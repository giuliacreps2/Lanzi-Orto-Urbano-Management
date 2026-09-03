package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;

import java.util.UUID;


public interface ICartService {
    Cart getActiveCartByUserId(User currentUser, CartStatus cartStatus);

    Cart getActiveCartByUserId(User currentUser);

    Cart findById(UUID cartId);

    Cart refreshCartTotal(Cart cart);

    void clearCart(UUID cartId);

    void markCartAsConverted(Cart cart, Order order);

    Cart getActiveCartByEmail(String email);

    Cart getActiveCartByEmail(String email, CartStatus cartStatus);

    Cart getCartForCheckout(UUID cartId, User currentUser, String guestEmail);
}
