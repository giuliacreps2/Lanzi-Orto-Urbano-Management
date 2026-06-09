package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import java.util.UUID;

public record ProductInfoDTO(
        UUID productId,
        String productName,
        String productSlug,
        String productDescription,
        String shortProductDescription,
        boolean isAvailable,
        String categoryName
) {
}