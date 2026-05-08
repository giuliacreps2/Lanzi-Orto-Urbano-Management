package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record VerifyUserDTO(
        String email,
        String verificationCode
) {
}
