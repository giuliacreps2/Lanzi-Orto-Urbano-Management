package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;

public record InventoryDTO(
        String inventoryName,
        Integer currentQuantity,
        Unit unit,
        Integer minThreshold
) {
}
