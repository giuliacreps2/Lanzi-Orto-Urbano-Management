package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.LoginDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.LoginRespDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.NewUserRespDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RegisterUserDTO;
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
    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO verifyB2c(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateUser(token);
    }
}
