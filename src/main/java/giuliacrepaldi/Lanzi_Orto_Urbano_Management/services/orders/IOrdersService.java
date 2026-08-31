package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.CheckoutRequestDTO;

import java.util.UUID;

public interface IOrdersService {

    Order createOrderFromCart(User currentUser, CheckoutRequestDTO body);

    void cancelOrder(UUID orderId);
}
