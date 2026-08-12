package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.AccountType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.LoggedUserDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.LoginDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.LoginRespDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.tools.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthService1 {
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

    public AuthService1(UsersService usersService, UsersRepository usersRepository, TokenTools tokenTools, RegistrationRequestsService registrationRequestsService, RegistrationRequestsRepository registrationRequestsRepository, PasswordEncoder bcrypt, RolesRepository rolesRepository, RolesService rolesService, B2cProfilesRepository b2cProfilesRepository, UsersRolesService usersRolesService, B2bProfilesRepository b2bProfilesRepository, AdminProfilesRepository adminProfilesRepository, AdminProfilesService adminProfilesService, EmailSender emailSender) {
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
    }


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
        } else if (found.getB2cProfile() != null) {
            accountType = AccountType.B2B;
        } else if (found.getB2bProfile() != null) {
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


}
