package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.InvMovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InventoryMovementDTO(
        double quantityInvMovement,
        BigDecimal priceInvMovement,
        String reasonInvMovement,
        LocalDateTime createdAtInvMovement,
        InvMovementType invMovementType,
        UUID inventoryId

) {
}
