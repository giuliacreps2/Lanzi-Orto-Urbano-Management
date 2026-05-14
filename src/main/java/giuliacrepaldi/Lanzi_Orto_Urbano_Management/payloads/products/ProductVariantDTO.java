package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

import java.util.UUID;

public record ProductVariantDTO(
        String skuVariant,
        boolean activeVariant,
        double netWeight,
        Unit unit,
        UUID productId,
        UUID packTypeId,
        UUID priceListId
) {
}
