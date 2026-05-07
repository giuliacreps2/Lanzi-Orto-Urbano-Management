package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import jakarta.validation.constraints.NotBlank;

public record RoleDTO(
        @NotBlank(message = "Role can't be blank")
        String roleName
) {
}
