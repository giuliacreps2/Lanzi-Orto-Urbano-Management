package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemsService implements ICartItemService {
    @Override
    public CartItem addItemToCart(Cart cart, UUID variantId, int quantity) {
        return null;
    }

    @Override
    public CartItem updateItemQuantity(UUID cartItemId, int quantity) {
        return null;
    }

    @Override
    public void removeItemFromCart(Cart cart, UUID cartItemId) {

    }

    @Override
    public CartItem getCartItem(UUID cartId) {
        return null;
    }
}
