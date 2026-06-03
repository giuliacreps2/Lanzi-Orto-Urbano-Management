package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import java.time.LocalDateTime;
import java.util.Map;

public record AdminOrderDeliveryDTO(
        String recipientName,
        LocalDateTime deliveryDate,
        String statusDeliveryType,
        Map<String, Object> shippingAddress
) {
}
