package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }


    //REGISTRAZIONE USER AND B2C
    @PostMapping("/register/b2c")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerB2c(@RequestBody @Validated RegisterUserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.authService.registerNewB2cProfile(body);
    }

    //VERIFICA EMAIL
    @GetMapping("/verify/b2c")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO verifyB2c(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateUser(token);
    }

    //REGISTRAZIONE B2B
    @PostMapping("/register/b2b")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerB2b(@RequestBody @Validated RegisterB2bProfileDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.authService.registerNewB2bProfile(body);
    }

    // VERIFICA EMAIL B2B
    @GetMapping("/verify/b2b")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO verifyB2b(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateB2bProfile(token);
    }

}
