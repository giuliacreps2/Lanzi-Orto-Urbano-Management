package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderSummaryDTO(
        UUID orderId,
        String orderNumber,
        StatusOrder status,
        LocalDateTime createdAt,
        BigDecimal totalAmount,
        List<OrderItemSummaryDTO> items
) {
}
