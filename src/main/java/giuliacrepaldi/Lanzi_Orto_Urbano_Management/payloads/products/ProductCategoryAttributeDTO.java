package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AttributeType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

public record ProductCategoryAttributeDTO(
        String prodCatAttributeKey,
        String prodCatAttributeLabel,
        AttributeType attrType,
        boolean required,
        String defaultValue,
        String minValue,
        String maxValue,
        Unit unit,
        ProductCategory productCategory

) {
}
