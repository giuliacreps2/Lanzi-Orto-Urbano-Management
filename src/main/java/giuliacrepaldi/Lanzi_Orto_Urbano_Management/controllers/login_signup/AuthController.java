package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.cookie.CookieUtils;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.UnauthorizedException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.security.TokenTools;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final TokenTools tokenTools;

    public AuthController(AuthService authService, CookieUtils cookieUtils, TokenTools tokenTools) {
        this.authService = authService;
        this.cookieUtils = cookieUtils;
        this.tokenTools = tokenTools;
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

    //----------------------------RESET PASSWORD----------------------------//

    @PostMapping("/password-reset/request")
    @ResponseStatus(HttpStatus.OK)
    public void requestPasswordReset(@RequestBody RequestNewPasswordDTO body) {
        this.authService.requestPasswordReset(body.email());
    }

    @PostMapping("/password-reset/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmPasswordReset(@Valid @RequestBody ConfirmNewPasswordDTO body) {
        this.authService.resetPassword(body.token(), body.newPassword());
    }

    //------------------------------SIGN IN--------------------------------//


    //------B2C AND B2B AS B2C--------//
    //STEP. 1

    //RICHIESTA
    @PostMapping("/register/new-user")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerNewB2c(@RequestBody @Validated RegisterAccountUserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        String message = this.authService.registerNewUserAccount(body);
        return Map.of("message", message);
    }


    //STEP. 2

    //VERIFICA EMAIL
    @GetMapping("/verify/new-account")
    @ResponseStatus(HttpStatus.OK)
    public NewUserRespDTO verifyB2c(@RequestParam("token") String token, HttpServletResponse response) {
        User user = this.authService.verifyAndCreateB2cAccount(token);
        String accessToken = this.tokenTools.generateToken(user);

        ResponseCookie cookie = cookieUtils.createAccessTokenCookie(accessToken, Duration.ofDays(7));
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new NewUserRespDTO(user.getUserId());
    }

//    @GetMapping("/verify/account/b2b")
//    @ResponseStatus(HttpStatus.OK)
//    public NewUserRespDTO verifyNewB2b(@RequestParam("token") String token, HttpServletResponse response) {
//        NewUserRespDTO result = this.authService.verifyAndCreateB2cAccount(token);
//        User user = this.usersService.findById(result.userId());
//        String accessToken = this.tokenTools.generateToken(user);
//        ResponseCookie cookie = cookieUtils.createAccessTokenCookie(accessToken, Duration.ofDays(7));
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//        return result;
//    }

    //-------------------------------------------------B2B---------------------------------------//


    //REGISTRAZIONE B2B
    @PostMapping("/register/b2b/complete-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public B2bProfileRespDTO registerB2b(@RequestBody @Validated CompleteB2bProfileDTO body, BindingResult validationResult, @AuthenticationPrincipal User authenticatedUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.authService.createB2bProfile(authenticatedUser, body);
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
