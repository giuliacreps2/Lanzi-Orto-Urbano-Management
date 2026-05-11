package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final TokenTools tokenTools;
    private final RegistrationRequestsService registrationRequestsService;
    private final RegistrationRequestsRepository registrationRequestsRepository;
    private final PasswordEncoder bcrypt;
    private final RolesRepository rolesRepository;
    private final RolesService rolesService;
    private final B2cProfilesRepository b2cProfilesRepository;
    private final UsersRolesService usersRolesService;
    private final B2bProfilesRepository b2bProfilesRepository;
    private final AdminProfilesRepository adminProfilesRepository;
    private final AdminProfilesService adminProfilesService;

    public AuthService(UsersService usersService, UsersRepository usersRepository, TokenTools tokenTools, RegistrationRequestsService registrationRequestsService, RegistrationRequestsRepository registrationRequestsRepository, PasswordEncoder bcrypt, RolesRepository rolesRepository, RolesService rolesService, B2cProfilesRepository b2cProfilesRepository, UsersRolesService usersRolesService, B2bProfilesRepository b2bProfilesRepository, AdminProfilesRepository adminProfilesRepository, AdminProfilesService adminProfilesService) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.tokenTools = tokenTools;
        this.registrationRequestsService = registrationRequestsService;
        this.registrationRequestsRepository = registrationRequestsRepository;
        this.bcrypt = bcrypt;
        this.rolesRepository = rolesRepository;
        this.rolesService = rolesService;
        this.b2cProfilesRepository = b2cProfilesRepository;
        this.usersRolesService = usersRolesService;
        this.b2bProfilesRepository = b2bProfilesRepository;
        this.adminProfilesRepository = adminProfilesRepository;
        this.adminProfilesService = adminProfilesService;
    }

    //LOGIN ONLY
    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        try {
            User found = this.usersService.findByEmail(body.email());
            //Controllo psw
            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Invalid Credentials");
            }

        } catch (NotFoundException ex) {
            throw new NotFoundException("Invalid Credentials");
        }

    }

    //SIGN UP B2C
    public String registerNewB2cProfile(RegisterUserDTO body) {

        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("User with this email already exists");

        if (this.registrationRequestsRepository.existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.email(), LocalDateTime.now()))
            throw new BadRequestException("Check out in your registration request. Your token is here");

        RegistrationRequest newR = RegistrationRequest.builder()
                .email(body.email())
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .clientCategory(ClientCategory.B2C)
                .metadata(Map.of("name", body.name(),
                        "surname", body.surname(), "password", Objects.requireNonNull(this.bcrypt.encode(body.password())), "phoneNumber", body.phoneNumber()
                        , "privacyAccepted", body.privacyAccepted()))
                .build();

        RegistrationRequest savedR = this.registrationRequestsRepository.save(newR);

        //            this.emailSender.sendRegistrationEmail(savedR);

        log.info("New B2C registration request for: {}", body.email());
        return "Your registration has taken place. Please check your email to verify your account.";
    }


    //VERIFICA E CREAZIONE UTENTE E B2C
    @Transactional
    public NewUserRespDTO verifyAndCreateUser(String token) {

        RegistrationRequest found = this.registrationRequestsRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException("Token not found"));

        if (found.isUsed())
            throw new BadRequestException("Token is already used");

        if (found.getTokenExpiresAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Token is expired");

        Map<String, Object> metadata = found.getMetadata();

        User newUser = new User();
        newUser.setEmail(found.getEmail());
        newUser.setPassword((String) metadata.get("password"));
        newUser.setActive(true);
        newUser.setEmailVerified(true);
        newUser.setPrivacyAccepted((Boolean) metadata.get("privacyAccepted"));
        newUser.setPrivacyAcceptedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedNewUser = this.usersRepository.save(newUser);

        //CREAZIONE B2C PROFILE
        if (found.getClientCategory() == ClientCategory.B2C) {
            B2cProfile newB2cProfile = new B2cProfile();
            newB2cProfile.setName((String) metadata.get("name"));
            newB2cProfile.setSurname((String) metadata.get("surname"));
            newB2cProfile.setPhoneNumber((String) metadata.get("phoneNumber"));
            newB2cProfile.setLoyaltyPoints(20L);
            newB2cProfile.setUser(savedNewUser);
            b2cProfilesRepository.save(newB2cProfile);
        }

        Role newRole = this.rolesRepository.findByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("Role not found"));

        usersRolesService.saveUserRole(savedNewUser, newRole);

        found.setUsed(true);
        found.setUsedAt(LocalDateTime.now());
        this.registrationRequestsRepository.save(found);

        log.info("New B2cUser has been registered: {}", found);
        return new NewUserRespDTO(savedNewUser.getUserId());
    }


    //____________________________________B2B___________________________________//


    //SIGN UP B2B
    public String registerNewB2bProfile(RegisterB2bProfileDTO body) {

        if (this.usersRepository.existsByEmail(body.contactEmail()))
            throw new BadRequestException("User with this email already exists");

        if (this.registrationRequestsRepository.existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.contactEmail(), LocalDateTime.now()))
            throw new BadRequestException("Check out in your registration request. Your token is here");

        if (this.b2bProfilesRepository.existsByFiscalCode(body.fiscalCode()) || this.b2bProfilesRepository.existsByVatNumber(body.vatNumber()))
            throw new BadRequestException("User with this vat number or fiscal code already exists");

        RegistrationRequest newR = RegistrationRequest.builder()
                .email(body.contactEmail())
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .clientCategory(ClientCategory.B2B)
                .metadata(Map.of(
                        "contactName", body.contactName(),
                        "contactSurname", body.contactSurname(),
                        "contactEmail", body.contactEmail(),
                        "password", Objects.requireNonNull(this.bcrypt.encode(body.password())),
                        "contactPhone", body.contactPhone(),
                        "vatNumber", Objects.requireNonNullElse(body.vatNumber(), ""),
                        "fiscalCode", Objects.requireNonNullElse(body.fiscalCode(), ""),
                        "companyName", body.companyName(),
                        "typeActivity", body.typeActivity().name(),
                        "privacyAccepted", body.privacyAccepted()
                )).build();

        RegistrationRequest savedR = this.registrationRequestsRepository.save(newR);

        //            this.emailSender.sendRegistrationEmail(savedR);

        log.info("New B2B registration request for: {}", body.contactEmail());
        return "Your registration has taken place. Check your email address.";
    }

    //VERIFICA E CREAZIONE UTENTE B2B
    @Transactional
    public NewUserRespDTO verifyAndCreateB2bProfile(String token) {

        RegistrationRequest found = this.registrationRequestsRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException("Token already exists"));

        if (found.isUsed())
            throw new BadRequestException("Token is already used");

        if (found.getTokenExpiresAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Token is expired");

        Map<String, Object> metadata = found.getMetadata();

        User newUser = new User();
        newUser.setEmail(found.getEmail());
        newUser.setPassword((String) metadata.get("password"));
        newUser.setActive(false);
        newUser.setEmailVerified(true);
        newUser.setPrivacyAccepted((Boolean) metadata.get("privacyAccepted"));
        newUser.setPrivacyAcceptedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedNewUser = this.usersRepository.save(newUser);

        //CREAZIONE B2B PROFILE
        if (found.getClientCategory() == ClientCategory.B2B) {
            B2bProfile newB2bProfile = new B2bProfile();
            newB2bProfile.setContactName((String) metadata.get("contactName"));
            newB2bProfile.setContactSurname((String) metadata.get("contactSurname"));
            newB2bProfile.setContactPhone((String) metadata.get("contactPhone"));
            newB2bProfile.setContactEmail(found.getEmail());
            newB2bProfile.setVatNumber((String) metadata.get("vatNumber"));
            newB2bProfile.setFiscalCode((String) metadata.get("fiscalCode"));
            newB2bProfile.setCompanyName((String) metadata.get("companyName"));
            newB2bProfile.setTypeActivity(TypeActivity.valueOf((String) metadata.get("typeActivity")));
            newB2bProfile.setLoyaltyPoints(20L);
            newB2bProfile.setStatusB2b(StatusB2b.PENDING);
            newB2bProfile.setUser(savedNewUser);
            b2bProfilesRepository.save(newB2bProfile);
        }

        Role newRole = this.rolesRepository.findByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("Role not found"));


//        this.emailSender.sendRegistrationEmail(savedR);

        //Devo inviare una mail all'amministratore per la verifica del p.iva o del cf
        //Devo cambiare mettere lo stato pending, finchè l'amministratore non dà conferma
        //Devo inviare un messaggio all'utente con "Stiamo verificando i tuoi dati"
        //Devo cambiare lo stato da pending ad accepted
        //Se non viene accettato deve provare a riscrivere i dati
        //quindi il form nel frontend si resetta

        usersRolesService.saveUserRole(savedNewUser, newRole);

        found.setUsed(true);
        found.setUsedAt(LocalDateTime.now());
        this.registrationRequestsRepository.save(found);

        //      this.emailSender.notifyAdminForApproval(savedNewUser);

        return new NewUserRespDTO(savedNewUser.getUserId());
    }


    //APPROVAZIONE B2B DA PARTE DELL'ADMIN
    @Transactional
    public String approveB2bProfile(UUID userId) {

        User user = this.usersService.findById(userId);

        if (user.getB2bProfile() == null) throw new BadRequestException("This user has no B2B profile");

        if (user.getB2bProfile().getStatusB2b() == StatusB2b.APPROVED)
            throw new BadRequestException("This user has already approved B2B profile");

        user.getB2bProfile().setStatusB2b(StatusB2b.APPROVED);
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        this.usersRepository.save(user);

        //this.emailSender.sendApprovalEmail(user);

        log.info("B2B profile approved for userId: {}", userId);
        return "B2B profile has been approved successfully";
    }


    //____________________________________ADMIN___________________________________//

    //SIGN UP ADMIN PROFILE
    public String registerNewAdminProfile(RegisterAdminProfileDTO body) {
        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("User with this email already exists");

        if (this.registrationRequestsRepository
                .existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.email(), LocalDateTime.now()))
            throw new BadRequestException("A pending registration already exists for this email");

        RegistrationRequest newR = RegistrationRequest.builder()
                .email(body.email())
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .isUsed(false)
                .clientCategory(ClientCategory.B2C)
                .metadata(Map.of(
                        "name", body.name(),
                        "surname", body.surname(),
                        "password", this.bcrypt.encode(body.password()),
                        "role", "ADMIN"
                ))
                .build();

        this.registrationRequestsRepository.save(newR);
        log.info("New admin profile has been registered successfully: {}", body.email());
        return "New admin profile has been registered successfully";
    }

    //VERIFICA E CREAZIONE ADMIN
    @Transactional
    public NewUserRespDTO verifyAndCreateAdminRole(String token) {

        RegistrationRequest found = this.registrationRequestsRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException("Token already exists"));

        if (found.isUsed())
            throw new BadRequestException("Token is already used");

        if (found.getTokenExpiresAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Token is expired");

        Map<String, Object> metadata = found.getMetadata();

        User newUser = new User();
        newUser.setEmail(found.getEmail());
        newUser.setPassword((String) metadata.get("password"));
        newUser.setActive(true);
        newUser.setEmailVerified(true);
        newUser.setPrivacyAccepted(true);
        newUser.setPrivacyAcceptedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedNewUser = this.usersRepository.save(newUser);

        adminProfilesService.saveAdminProfile(
                savedNewUser,
                (String) metadata.get("name"),
                (String) metadata.get("surname")
        );

        Role newRole = this.rolesRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new NotFoundException("Role ADMIN not found"));

        usersRolesService.saveUserRole(savedNewUser, newRole);

        found.setUsed(true);
        found.setUsedAt(LocalDateTime.now());
        this.registrationRequestsRepository.save(found);

        return new NewUserRespDTO(savedNewUser.getUserId());
    }
}
