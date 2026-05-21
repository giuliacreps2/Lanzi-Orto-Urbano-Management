package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;

import java.util.UUID;

public record ProductCatalogDTO(
        UUID productId,
        UUID variantId,
        String productName,
        String productSlug,
        String shortProductDescription,
        String skuVariant,
        double netWeight,
        String unit,
        double price,
        ClientCategory clientCategory,
        String priceLabel,
        Integer minOrderQuantity,
        boolean productIsAvailable,
        boolean activeVariant
) {
}
