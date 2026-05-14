package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AttributeType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

import java.util.UUID;

public record ProductCategoryAttributeDTO(
        String prodCatAttributeKey,
        String prodCatAttributeLabel,
        AttributeType attrType,
        boolean required,
        String defaultValue,
        String minValue,
        String maxValue,
        Unit unit,
        UUID productCategoryId

) {
}
