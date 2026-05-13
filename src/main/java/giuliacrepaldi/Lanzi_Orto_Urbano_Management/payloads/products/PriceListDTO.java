package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;

public record PriceListDTO(
        double price,
        Integer minOrderQuantity,
        ClientCategory clientCategory,
        ProductVariant productVariant
) {
}
