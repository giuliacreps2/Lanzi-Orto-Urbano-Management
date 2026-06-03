package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.SourceOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdminOrderDetailDTO(
        UUID orderId,
        String orderNumber,
        StatusOrder statusOrder,
        SourceOrder sourceOrder,
        DeliveryType deliveryType,
        LocalDateTime orderCreatedAt,
        LocalDateTime orderUpdatedAt,
        BigDecimal totalAmount,
        String orderNotes,
        AdminOrderCustomerDTO customer,
        AdminOrderDeliveryDTO delivery,
        List<AdminOrderItemDTO> items
) {
}
