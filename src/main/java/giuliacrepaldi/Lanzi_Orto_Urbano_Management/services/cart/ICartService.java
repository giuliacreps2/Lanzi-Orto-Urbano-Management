package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;

import java.util.UUID;


public interface ICartService {
    Cart getActiveCartByUserId(User currentUser);

    Cart findById(UUID cartId);

    Cart refreshCartTotal(Cart cart);

    void clearCart(UUID cartId);

    void markCartAsConverted(Cart cart, Order order);

    Cart getActiveCartByEmail(String email);
}
