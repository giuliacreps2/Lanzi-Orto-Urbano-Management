package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.LoginDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersService usersService;
    private final TokenTools tokenTools;

    public AuthService(UsersService usersService, TokenTools tokenTools) {
        this.usersService = usersService;
        this.tokenTools = tokenTools;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        try {
            User found = this.usersService.findByEmail(body.email());

            //Controllo psw
            if (found.getPassword().equals(body.password())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Invalid Credentials");
            }
        } catch (NotFoundException ex) {
            throw new NotFoundException("Invalid Credentials");
        }
    }
}
