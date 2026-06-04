package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.PaymentType;

import java.util.List;
import java.util.Map;

public record CheckoutDTO(
        List<OrderItemDTO> items,
        DeliveryType deliveryType,
        boolean loyaltyPointsUsed,
        PaymentType paymentType,
        Map<String, Object> billingDetails
) {
}
