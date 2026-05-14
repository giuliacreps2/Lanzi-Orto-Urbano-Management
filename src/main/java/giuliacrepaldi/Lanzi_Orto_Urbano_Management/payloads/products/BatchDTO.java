package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.StatusBatch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record BatchDTO(
        String batchCode,
        StatusBatch statusBatch,
        double quantityPlanned,
        double quantityActual,
        LocalDateTime startedAt,
        LocalDate expectedHarvestDate,
        LocalDate actualHarvestDate,
        Map<String, Object> batchMetadata,
        UUID productVariantId
) {
}
