package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.PaymentType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.SourceOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CheckoutDTO(
        UUID b2cProfileId,
        UUID b2bProfileId,
        List<OrderItemDTO> items,
        StatusOrder statusOrder,
        SourceOrder sourceOrder,
        DeliveryType deliveryType,
        boolean reorderedFormByAdmin,
        boolean loyaltyPointsUsed,
        PaymentType paymentType,
        Map<String, Object> billingDetails
) {
}
