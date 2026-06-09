package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

import java.util.UUID;

public record ProductCatalogDTO(
        UUID productId,
        UUID variantId,
        String productName,
        String productSlug,
        String shortProductDescription,
        String skuVariant,
        double netWeight,
        Unit unit,
        java.math.BigDecimal price,
        ClientCategory clientCategory,
        String priceLabel,
        Integer minOrderQuantity,
        boolean productIsAvailable,
        boolean activeVariant
) {
}
