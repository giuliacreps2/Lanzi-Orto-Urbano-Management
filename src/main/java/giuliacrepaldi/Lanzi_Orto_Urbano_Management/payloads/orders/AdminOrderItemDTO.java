package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record AdminOrderItemDTO(
        UUID orderItemId,
        Integer quantity,
        BigDecimal price,

        String productName,
        String categoryName,
        UUID variantId,
        String skuVariant,
        Double netWeight,
        String unit,

        UUID batchId,
        String batchCode,
        LocalDate expectedHarvestDate,

        Map<String, Object> technicalDetails,
        Integer labelsCount
) {
}
