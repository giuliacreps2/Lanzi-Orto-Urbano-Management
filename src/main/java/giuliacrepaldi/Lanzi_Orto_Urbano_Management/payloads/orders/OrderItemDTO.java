package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import java.util.UUID;

public record OrderItemDTO(
        UUID variantId,
        Integer quantity
) {
}
