package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import jakarta.validation.constraints.NotBlank;

public record RoleDTO(
        @NotBlank(message = "Role can't be blank")
        String roleName
) {
}
