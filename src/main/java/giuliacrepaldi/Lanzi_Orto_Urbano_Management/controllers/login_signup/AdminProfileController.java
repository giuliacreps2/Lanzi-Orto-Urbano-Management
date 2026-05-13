package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.NewUserRespDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.RegisterAdminProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth/admin")
public class AdminProfileController {

    private final AuthService authService;

    public AdminProfileController(AuthService authService) {
        this.authService = authService;
    }


    //REGISTRAZIONE NUOVO ADMIN
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerAdminProfile(@RequestBody @Validated RegisterAdminProfileDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.authService.registerNewAdminProfile(body);
    }


    //VERIFICA TOKEN E CREAZIONE ADMIN
    @GetMapping("/verify/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO verifyAdmin(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateAdminRole(token);
    }

    @PostMapping("/approved-b2b/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String approveB2b(@PathVariable UUID userId) {
        return this.authService.approveB2bProfile(userId);
    }
}
