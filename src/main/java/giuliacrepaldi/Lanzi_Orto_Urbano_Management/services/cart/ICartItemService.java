package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.CartItem;

import java.util.UUID;

public interface ICartItemService {
    CartItem addItemToCart(Cart cart, UUID variantId, int quantity);

    CartItem updateItemQuantity(UUID cartItemId, int quantity);

    void removeItemFromCart(Cart cart, UUID cartItemId);

    CartItem getCartItem(UUID cartItemId);
}
