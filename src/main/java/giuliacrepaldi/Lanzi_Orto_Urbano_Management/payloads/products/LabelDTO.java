package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LabelDTO(
        String barCodeGs1,
        Integer barcodeData,
        LocalDate productionDate,
        LocalDate bestBeforeDate,
        LocalDate exitDate,
        LocalDateTime printedAt,
        boolean inventoryDecremented,
        Batch batch,
        ProductVariant productVariant
) {
}
