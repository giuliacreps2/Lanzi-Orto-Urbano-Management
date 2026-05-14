package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;

import java.util.UUID;

public record PriceListDTO(
        double price,
        Integer minOrderQuantity,
        ClientCategory clientCategory,
        UUID productVariantId
) {
}
