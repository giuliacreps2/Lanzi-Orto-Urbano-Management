package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.ProductStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDTO(
        String productName,
        String productSlug,
        String productDescription,
        String shortProductDescription,
        AvailabilityStatus availabilityStatus,
        boolean productIsAvailable,
        LocalDateTime createdAt,
        ProductStatus productStatus,
        UUID productCategoryId
) {
}
