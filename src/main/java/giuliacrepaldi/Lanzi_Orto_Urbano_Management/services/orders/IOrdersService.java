package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;

import java.util.UUID;

public interface IOrdersService {

    Order createOrderFromCart(UUID cartId);

    void cancelOrder(UUID orderId);
}
