package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.CREATED)
//    public LoginRespDTO login(@RequestBody LoginDTO body) {
//        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
//    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return this.authService.login(body);
    }


    //REGISTRAZIONE USER AND B2C
//    @PostMapping("/register/b2c")
//    @ResponseStatus(HttpStatus.CREATED)
//    public RegistrationRequestsDTO registerB2c(@RequestBody @Validated RegisterUserDTO body, BindingResult validationResult) {
//        if (validationResult.hasErrors()) {
//            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
//            throw new ValidationException(errors);
//        }
//        String message = this.authService.registerNewB2cProfile(body);
//
//        if (message == null) {
//            message = "Register successfully";
//        }
//
//        return
//    }

    @PostMapping("/register/b2c")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationRequest registerB2c(@RequestBody @Validated RegisterUserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
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
    public Map<String, String> registerB2b(@RequestBody @Validated RegisterB2bProfileDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        String message = this.authService.registerNewB2bProfile(body);
        return Map.of("message", message);
    }

    // VERIFICA EMAIL B2B
    @GetMapping("/verify/b2b")
    @ResponseStatus(HttpStatus.OK)
    public NewUserRespDTO verifyB2b(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateB2bProfile(token);
    }

    //APPROVED B2B
    @GetMapping("/b2b/{userId}/approve")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> approveB2b(@PathVariable UUID userId) {
        String msg = this.authService.approveB2bProfile(userId);
        return Map.of("message", msg);
    }

    //REJECTED B2B
    @PatchMapping("/b2b/{userId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> rejectB2b(@PathVariable UUID userId, @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        String msg = this.authService.rejectB2bProfile(userId, reason);
        return Map.of("message", msg);
    }

}
