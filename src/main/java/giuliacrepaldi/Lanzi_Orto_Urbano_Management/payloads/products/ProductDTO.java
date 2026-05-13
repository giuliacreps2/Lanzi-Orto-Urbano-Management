package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ProductDTO(
        String productName,
        String productSlug,
        String productDescription,
        String shortProductDescription,
        AvailabilityStatus availabilityStatus,
        boolean productIsAvailable,
        Map<String, Object> technicalProdDetails,
        LocalDateTime createdAt,
        ProductCategory productCategory
) {
}
