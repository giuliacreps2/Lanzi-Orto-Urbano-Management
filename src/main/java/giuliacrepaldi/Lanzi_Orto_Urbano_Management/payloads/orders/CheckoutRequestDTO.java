package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.PaymentMethod;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;

import java.util.UUID;

public record CheckoutRequestDTO(
        UUID cartId,
        PaymentMethod paymentMethod,
        DeliveryType deliveryType,
        Integer pointsToRedeem,
        String shippingAddressNotes,
        String guestEmail,
        String guestName
) {

}
