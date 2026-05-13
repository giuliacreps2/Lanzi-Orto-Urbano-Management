package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import jakarta.validation.constraints.NotBlank;

public record ProvincesDTO(
        @NotBlank(message = "Acronym is required")
        String acronym,
        @NotBlank(message = "Province's name is required")
        String provinceName
) {
}
