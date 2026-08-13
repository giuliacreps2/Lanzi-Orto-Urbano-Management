package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import jakarta.validation.constraints.NotBlank;

public record ConfirmNewPasswordDTO(
        @NotBlank String token,
        @NotBlank String newPassword
) {
}