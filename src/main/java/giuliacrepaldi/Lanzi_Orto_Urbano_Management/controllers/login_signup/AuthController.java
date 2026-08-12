package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.cookie.CookieUtils;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.RegistrationRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    public AuthController(AuthService authService, CookieUtils cookieUtils) {
        this.authService = authService;
        this.cookieUtils = cookieUtils;
    }


    //-----------------------------------LOGIN----------------------------//

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoggedUserDTO login(@RequestBody LoginDTO body, HttpServletResponse response) {
        LoginRespDTO result = this.authService.login(body);

        ResponseCookie cookie = cookieUtils.createAccessTokenCookie(result.accessToken(), Duration.ofDays(7));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return result.user();
    }


    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.clearAccessTokenCookie().toString());
        return Map.of("message", "Logout effettuato con successo");
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public LoggedUserDTO me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new UnauthorizedException("Not authenticated");
        }
        return this.authService.getCurrentUser(user);
    }


    //------------------------------
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


    //-------------------------------------------------B2B---------------------------------------//

    //STEP 1. SIGN UP B2B AS B2C
    @PostMapping("/register/b2b-user")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerNewB2b(@RequestBody @Validated RegisterB2bAccountDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        String message = this.authService.registerNewB2bAccount(body);
        return Map.of("message", message);
    }

    //STEP 2. VERIFICATION USER B2B AS B2C
    @GetMapping("/verify/account/b2b")
    @ResponseStatus(HttpStatus.OK)
    public NewUserRespDTO verifyNewB2b(@RequestParam("token") String token) {
        return this.authService.verifyAndCreateB2bAccount(token);
    }

//
//    //REGISTRAZIONE B2B
//    @PostMapping("/register/b2b")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Map<String, String> registerB2b(@RequestBody @Validated RegisterB2bProfileDTO body, BindingResult validationResult) {
//        if (validationResult.hasErrors()) {
//            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
//            throw new ValidationException(errors);
//        }
//        String message = this.authService.registerNewB2bProfile(body);
//        return Map.of("message", message);
//    }
//
//    // VERIFICA EMAIL B2B
//    @GetMapping("/verify/b2b")
//    @ResponseStatus(HttpStatus.OK)
//    public NewUserRespDTO verifyB2b(@RequestParam("token") String token) {
//        return this.authService.verifyAndCreateB2bProfile(token);
//    }

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
