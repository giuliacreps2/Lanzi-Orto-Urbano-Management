package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.PaymentMethod;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;

public record CheckoutRequestDTO(
        PaymentMethod paymentMethod,
        Integer pointsToRedeem,
        String shippingAddressNotes,
        DeliveryType deliveryType,
        String guestEmail,
        String guestName
) {

}
