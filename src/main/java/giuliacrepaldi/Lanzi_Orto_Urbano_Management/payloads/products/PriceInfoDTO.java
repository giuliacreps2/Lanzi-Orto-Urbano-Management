package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;

import java.math.BigDecimal;

public record PriceInfoDTO(
        BigDecimal price,
        Integer minOrderQuantity,
        ClientCategory clientCategory
) {
}
