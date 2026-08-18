package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.AccountType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.tools.EmailSender;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


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
    private final EmailSender emailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final B2bProfilesService b2bProfilesService;
    private final MunicipalitiesRepository municipalitiesRepository;
    private final AddressesRepository addressesRepository;


    public AuthService(UsersService usersService, UsersRepository usersRepository, TokenTools tokenTools, RegistrationRequestsService registrationRequestsService, RegistrationRequestsRepository registrationRequestsRepository, PasswordEncoder bcrypt, RolesRepository rolesRepository, RolesService rolesService, B2cProfilesRepository b2cProfilesRepository, UsersRolesService usersRolesService, B2bProfilesRepository b2bProfilesRepository, AdminProfilesRepository adminProfilesRepository, AdminProfilesService adminProfilesService, EmailSender emailSender, PasswordResetTokenRepository passwordResetTokenRepository, B2bProfilesService b2bProfilesService, MunicipalitiesRepository municipalitiesRepository, AddressesRepository addressesRepository) {
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
        this.emailSender = emailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.b2bProfilesService = b2bProfilesService;
        this.municipalitiesRepository = municipalitiesRepository;
        this.addressesRepository = addressesRepository;
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
//
//    //VERIFICA E CREAZIONE ADMIN
//    @Transactional
//    public NewUserRespDTO verifyAndCreateAdminRole(String token) {
//
//        RegistrationRequest found = this.registrationRequestsRepository.findByVerificationToken(token)
//                .orElseThrow(() -> new NotFoundException("Token already exists"));
//
//        if (found.isUsed())
//            throw new BadRequestException("Token is already used");
//
//        if (found.getTokenExpiresAt().isBefore(LocalDateTime.now()))
//            throw new BadRequestException("Token is expired");
//
//        Map<String, Object> metadata = found.getMetadata();
//
//        User newUser = new User();
//        newUser.setEmail(found.getEmail());
//        newUser.setPassword((String) metadata.get("password"));
//        newUser.setActive(true);
//        newUser.setEmailVerified(true);
//        newUser.setPrivacyAccepted(true);
//        newUser.setPrivacyAcceptedAt(LocalDateTime.now());
//        newUser.setCreatedAt(LocalDateTime.now());
//        newUser.setUpdatedAt(LocalDateTime.now());
//
//        User savedNewUser = this.usersRepository.save(newUser);
//
//        adminProfilesService.saveAdminProfile(
//                savedNewUser,
//                (String) metadata.get("name"),
//                (String) metadata.get("surname")
//        );
//
//        Role newRole = this.rolesRepository.findByRoleName("ADMIN")
//                .orElseThrow(() -> new NotFoundException("Role ADMIN not found"));
//
//        usersRolesService.saveUserRole(savedNewUser, newRole);
//
//        found.setUsed(true);
//        found.setUsedAt(LocalDateTime.now());
//        this.registrationRequestsRepository.save(found);
//
//        return new NewUserRespDTO(savedNewUser.getUserId(), );
//
//    }


    //---------------------------------------LOGIN-----------------------------------------//

    //LOGIN ONLY
    public LoginRespDTO login(LoginDTO body) {
        User found = this.usersService.findByEmail(body.email());

        if (!this.bcrypt.matches(body.password(), found.getPassword())) {
            throw new UnauthorizedException("Invalid Credentials");
        }

        String token = this.tokenTools.generateToken(found);

        List<String> roles = this.usersRolesService
                .findRoleByUser(found)
                .stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .toList();

        return new LoginRespDTO(token, buildLoggedUserDTO(found, roles));
    }

    public LoggedUserDTO getCurrentUser(User found) {
        List<String> roles = this.usersRolesService
                .findRoleByUser(found)
                .stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .toList();

        return buildLoggedUserDTO(found, roles);
    }

    private LoggedUserDTO buildLoggedUserDTO(User found, List<String> roles) {
        AccountType accountType;

        if (roles.contains(AccountType.ADMIN.toString())) {
            accountType = AccountType.ADMIN;
        } else if (found.getB2bProfile() != null) {
            accountType = AccountType.B2B;
        } else if (found.getB2cProfile() != null) {
            accountType = AccountType.B2C;
        } else {
            accountType = AccountType.UNKNOW;
        }

        StatusB2b statusB2b = found.getB2bProfile() != null ? found.getB2bProfile().getStatusB2b() : null;

        return new LoggedUserDTO(
                found.getUserId(),
                found.getEmail(),
                roles,
                accountType,
                statusB2b,
                found.isActive(),
                found.isEmailVerified()
        );
    }


    //--------------------------------RESET PASSWORD---------------------------------------------//

    public void requestPasswordReset(String email) {

        try {
            Optional<User> found = this.usersRepository.findByEmail(email);

            if (found.isPresent()) {

                List<PasswordResetToken> oldTokens = this.passwordResetTokenRepository
                        .findByUserAndUsedFalse(found.get());
                oldTokens.forEach(t -> t.setUsed(true));
                this.passwordResetTokenRepository.saveAll(oldTokens);


                String newToken = UUID.randomUUID().toString();

                PasswordResetToken newPasswordReq = PasswordResetToken.builder()
                        .user(found.get())
                        .resetToken(newToken)
                        .expiresAt(LocalDateTime.now().plusMinutes(30))
                        .used(false)
                        .build();

                PasswordResetToken savedNewPasswordReq = this.passwordResetTokenRepository.save(newPasswordReq);

                this.emailSender.sendNewPassword(savedNewPasswordReq);


                log.info("Send new password request: {}", email);

            }
        } catch (Exception e) {
            log.error("Errore invio email reset password per {}: {}", email, e.getMessage());
        }
    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken found = this.passwordResetTokenRepository.findByResetToken(token);

        if (found == null) {
            throw new BadRequestException("Token not valid");
        }
        if (found.isUsed()) {
            throw new BadRequestException("Token has been used");
        }
        if (found.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired");
        }

        User user = found.getUser();
        user.setPassword(bcrypt.encode(newPassword));
        this.usersRepository.save(user);

        found.setUsed(true);
        this.passwordResetTokenRepository.save(found);

        this.emailSender.confirmNewPassword(found);

        log.info("Password reset completed for user: {}", user.getEmail());

    }


    //-------------------------------------PROCESSO DI REGISTRAZIONE-----------------------------------------//

    //------B2C AND B2B AS B2C--------//
    //STEP 1.

    //RICHIESTA SIGN UP
    public String registerNewUserAccount(RegisterAccountUserDTO body) {

        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("User with this email already exists");

        if (this.registrationRequestsRepository.existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.email(), LocalDateTime.now()))
            throw new BadRequestException("Check out in your registration request. Your token is here");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("password", this.bcrypt.encode(body.password()));
        metadata.put("privacyAccepted", body.privacyAccepted());
        metadata.put("intendedAccountType",
                body.intendedAccountType() != null ? body.intendedAccountType().toString() : "B2C");

        String token = UUID.randomUUID().toString();

        RegistrationRequest newR = RegistrationRequest.builder()
                .email(body.email())
                .verificationToken(token)
                .tokenExpiresAt(LocalDateTime.now().plusDays(1))
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .clientCategory(ClientCategory.B2C)
                .metadata(metadata)
                .build();

        RegistrationRequest savedR = this.registrationRequestsRepository.save(newR);

        this.emailSender.sendRegistrationEmail(savedR);

        this.emailSender.sendRegistrationEmailAdmin(savedR);


        log.info("New B2C registration request for: {}", body.email());

        // Ritorna l'oggetto persistito sul DB (avrà anche l'ID generato se usi una chiave sequenziale/UUID)
        return "Your registration request has been successfully completed";
    }


    //STEP 2.

    //VERIFICA E CREAZIONE ACCOUNT
    @Transactional
    public User verifyAndCreateB2cAccount(String token) {

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
        String intendedType = (String) metadata.getOrDefault("intendedAccountType", "B2C");
        newUser.setIntendedAccountType(AccountType.valueOf(intendedType));
        newUser.setPrivacyAcceptedAt(LocalDateTime.now());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        User savedNewUser = this.usersRepository.save(newUser);


        B2cProfile newB2cProfile = new B2cProfile();
        newB2cProfile.setLoyaltyPoints(20L);
        newB2cProfile.setUser(savedNewUser);
        b2cProfilesRepository.save(newB2cProfile);


        Role newRole = this.rolesRepository.findByRoleName("USER")
                .orElseThrow(() -> new NotFoundException("Role not found"));

        usersRolesService.saveUserRole(savedNewUser, newRole);

        found.setUsed(true);
        found.setUsedAt(LocalDateTime.now());
        this.registrationRequestsRepository.save(found);

        emailSender.sendWelcomeEmail(savedNewUser);

        log.info("New B2cUser has been registered: {}", found);
        return savedNewUser;
    }


    //----------B2B--------//

    //STEP 3.

    //REGISTRAZIONE NUOVO UTENTE B2B
    @Transactional
    public B2bProfileRespDTO createB2bProfile(User authenticatedUser, CompleteB2bProfileDTO body) {
        boolean hasVatNumber = body.vatNumber() != null && !body.vatNumber().isBlank();
        boolean hasFiscalCode = body.fiscalCode() != null && !body.fiscalCode().isBlank();

        if (authenticatedUser.getB2bProfile() != null)
            throw new BadRequestException("This user already has a B2B profile");

        if (!hasVatNumber && !hasFiscalCode)
            throw new BadRequestException("VAT number or fiscal code is required");

        if (hasVatNumber && this.b2bProfilesRepository.existsByVatNumber(body.vatNumber()))
            throw new BadRequestException("VAT number already exists");

        if (hasFiscalCode && this.b2bProfilesRepository.existsByFiscalCode(body.fiscalCode()))
            throw new BadRequestException("Fiscal code already exists");

        Municipality municipality = this.municipalitiesRepository.findById(body.addressDTO().municipalityId())
                .orElseThrow(() -> new NotFoundException("Municipality not found"));

        Address newAddress = new Address(
                body.addressDTO().street(),
                body.addressDTO().number(),
                body.addressDTO().locality(),
                body.addressDTO().postalCode(),
                municipality
        );
        Address savedAddress = this.addressesRepository.save(newAddress);

        String contactEmail = (body.contactEmail() != null && !body.contactEmail().isBlank())
                ? body.contactEmail()
                : authenticatedUser.getEmail();

        B2bProfile newB2bProfile = new B2bProfile();
        newB2bProfile.setVatNumber(body.vatNumber());
        newB2bProfile.setFiscalCode(body.fiscalCode());
        newB2bProfile.setPec(body.pec());
        newB2bProfile.setSdi_code(body.sdiCode());
        newB2bProfile.setContactPhone(body.contactPhone());
        newB2bProfile.setContactEmail(contactEmail);
        newB2bProfile.setCompanyName(body.companyName());
        newB2bProfile.setTypeActivity(body.typeActivity());
        newB2bProfile.setLoyaltyPoints(0L);
        newB2bProfile.setStatusB2b(StatusB2b.PENDING);
        newB2bProfile.setUser(authenticatedUser);
        newB2bProfile.setLegalAddress(savedAddress);

        B2bProfile savedB2bProfile = this.b2bProfilesRepository.save(newB2bProfile);

        this.emailSender.sendB2bPendingEmail(authenticatedUser.getEmail());
        this.emailSender.notifyAdminForApproval(authenticatedUser.getUserId(), savedB2bProfile);

        log.info("B2B profile submitted for review, userId: {}", authenticatedUser.getUserId());
        return new B2bProfileRespDTO(savedB2bProfile.getB2bProfileId(), savedB2bProfile.getStatusB2b());
    }


    //STEP 4.1

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

        this.emailSender.sendApprovalEmail(user.getEmail(), user.getB2bProfile().getContactName());

        log.info("B2B profile approved for userId: {}", userId);
        return "B2B profile has been approved successfully";
    }


    //STEP 4.2

    //REJECTED
    @Transactional
    public String rejectB2bProfile(UUID userId, String reason) {
        User user = this.usersService.findById(userId);

        if (user.getB2bProfile() == null)
            throw new BadRequestException("This user has no B2B profile");

        if (user.getB2bProfile().getStatusB2b() == StatusB2b.APPROVED)
            throw new BadRequestException("Cannot reject an already approved profile");

        if (user.getB2bProfile().getStatusB2b() == StatusB2b.REJECTED)
            throw new BadRequestException("Profile already rejected");

        user.getB2bProfile().setStatusB2b(StatusB2b.REJECTED);
        user.getB2bProfile().setNotes(reason);
        user.setUpdatedAt(LocalDateTime.now());
        this.usersRepository.save(user);

        this.emailSender.sendRejectionEmail(user.getEmail(), user.getB2bProfile().getContactName(), reason);

        log.info("B2B profile rejected for userId: {}", userId);
        return "B2B profile has been rejected";
    }


}