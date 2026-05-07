package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Province;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record MunicipalitiesDTO(
        @NotBlank(message = "Municipality's name is mandatory")
        String municipalityName,
        
        //TODO: Controlla @NotEmpty se è corretto per un'entity
        @NotEmpty(message = "This field is required")
        Province province
) {
}
