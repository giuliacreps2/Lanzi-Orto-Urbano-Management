package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

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
}
