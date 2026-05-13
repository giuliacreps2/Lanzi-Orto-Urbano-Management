package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

public record VerifyUserDTO(
        String email,
        String verificationCode
) {
}
