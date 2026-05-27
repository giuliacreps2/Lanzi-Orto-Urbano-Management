package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.ProductStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.UUID;

public record ProductFormDTO(
        @NotBlank
        String productName,
        String productSlug,
        String productDescription,
        String shortProductDescription,
        AvailabilityStatus availabilityStatus,
        boolean productIsAvailable,
        ProductStatus productStatus,
        UUID productCategoryId,
        String skuVariant,
        boolean activeVariant,
        Double netWeight,
        Unit unit,
        Map<String, Object> technicalDetails,
        UUID packTypeId,

        Double b2cPrice,
        Double b2bPrice,

        Integer b2bMinOrderQuantity
) {
}
