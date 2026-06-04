package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LabelDTO(
        String barCodeGs1,
        String barcodeData,
        LocalDate productionDate,
        LocalDate bestBeforeDate,
        LocalDate exitDate,
        LocalDateTime printedAt,
        boolean inventoryDecremented,
        UUID batchId,
        UUID productVariantId
) {
}
