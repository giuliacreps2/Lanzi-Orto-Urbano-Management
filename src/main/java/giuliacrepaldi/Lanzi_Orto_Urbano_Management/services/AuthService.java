package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.RequestedRole;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.LoginDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RegisterB2bProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RegisterB2cProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.RegistrationRequestsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.UsersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
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

    public AuthService(UsersService usersService, UsersRepository usersRepository, TokenTools tokenTools, RegistrationRequestsService registrationRequestsService, RegistrationRequestsRepository registrationRequestsRepository, PasswordEncoder bcrypt) {
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.tokenTools = tokenTools;
        this.registrationRequestsService = registrationRequestsService;
        this.registrationRequestsRepository = registrationRequestsRepository;
        this.bcrypt = bcrypt;
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
    public B2cProfile registerNewB2cProfile(RegisterB2cProfileDTO body) {

        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("User with this email already exists");

        User found = this.usersService.findByEmail(body.email());

        if (this.registrationRequestsRepository.existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.email(), LocalDateTime.now()))
                    throw new BadRequestException("Check out in your registration request. Your token is here");

        RegistrationRequest newR = new RegistrationRequest(
                        newR.setVerificationToken(UUID.randomUUID().toString()),
                        newR.setTokenExpiresAt(LocalDateTime.now().plusDays(1)),
                        newR.setUsed(false),
                        newR.setRequestedRole(RequestedRole.B2C),
                        newR.setMetadata(Map.of( "name", body.name(),
                                "surname", body.surname(), "password", this.bcrypt.matches(body.password(), found.getPassword()) ));

                RegistrationRequest savedR = this.registrationRequestsRepository.save(newR);

    //            this.emailSender.sendRegistrationEmail(savedR);

                log.info("New RegistrationRequest: " + savedR.toString());
                log.info("Check your email");
                return ;
}

    //SIGN UP B2B
//    public B2cProfile registerNewB2cProfile(RegisterB2bProfileDTO body) {
//
//        if (this.usersRepository.existsByEmail(body.email()))
//            throw new BadRequestException("User with this email already exists");
//
//        User found = this.usersService.findByEmail(body.email());
//
//        if (this.registrationRequestsRepository.existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(body.email(), LocalDateTime.now()))
//            throw new BadRequestException("Check out in your registration request. Your token is here");
//
//        RegistrationRequest newR = new RegistrationRequest(
//                newR.setVerificationToken(UUID.randomUUID().toString()),
//                newR.setTokenExpiresAt(LocalDateTime.now().plusDays(1)),
//                newR.setUsed(false),
//                newR.setRequestedRole(RequestedRole.B2C),
//                newR.setMetadata(Map.of( "name", body.name(),
//                        "surname", body.surname(), "password", this.bcrypt.matches(body.password(), found.getPassword()) ));
//
//        RegistrationRequest savedR = this.registrationRequestsRepository.save(newR);
//
//        //            this.emailSender.sendRegistrationEmail(savedR);
//
//        log.info("New RegistrationRequest: " + savedR.toString());
//        log.info("Check your email");
//        return ;
//    }
}
