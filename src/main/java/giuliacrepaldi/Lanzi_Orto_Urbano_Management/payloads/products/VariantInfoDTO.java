package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record VariantInfoDTO(
        UUID variantId,
        String skuVariant,
        double netWeight,
        Unit unit,
        Map<String, Object> technicalDetails,
        UUID packTypeId,
        String namePackType,
        String unitOfMeasure,
        List<PriceInfoDTO> priceLists
) {
}
