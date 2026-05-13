package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.PackagingCategory;

import java.util.Map;

public record PackagingTypeDTO(
        String namePackType,
        String unitOfMeasure,
        Map<String, Object> dimensionsJsonb,
        PackagingCategory packagingCategory,
        ProductVariant productVariant
) {
}
