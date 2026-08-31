package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import java.util.UUID;

public interface IOrdersService {

//    private Order createOrderFromCart(User currentUser, CheckoutRequestDTO body);

    void cancelOrder(UUID orderId);
}
