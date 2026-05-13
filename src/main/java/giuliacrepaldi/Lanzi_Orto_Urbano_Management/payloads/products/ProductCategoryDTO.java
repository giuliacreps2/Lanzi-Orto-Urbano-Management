package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record ProductCategoryDTO(
        @NotBlank(message = "This field cannot be blank")
        String nameProdCategory,
        boolean requiresBatchTracking,
        Map<String, Object> metadataProdCategory
) {
}
