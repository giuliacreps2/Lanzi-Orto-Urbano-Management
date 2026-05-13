package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PackagingType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PriceList;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

public record ProductVariantDTO(
        String skuVariant,
        boolean activeVariant,
        double netWeight,
        Unit unit,
        Product product,
        PackagingType packagingType,
        PriceList priceList
) {
}
