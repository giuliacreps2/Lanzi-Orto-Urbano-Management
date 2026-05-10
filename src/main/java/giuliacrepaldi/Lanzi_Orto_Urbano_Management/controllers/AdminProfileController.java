package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AdminProfileController {

    private final AuthService authService;

    public AdminProfileController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public


    @GetMapping("/verify/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public NewAdminRespDTO verifyAdmin(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateAdminRole(token);
    }

    @PostMapping("/user/{b2bProfileId}/approved-b2b")
    @ResponseStatus(HttpStatus.CREATED)
    public B2bProfile verifyAndCreateB2bProfile(@RequestParam("token") String token, @PathVariable("b2bProfileId") String b2bProfileId) {
        
        return this.authService.verifyAndCreateB2bProfile(token);
    }
}
