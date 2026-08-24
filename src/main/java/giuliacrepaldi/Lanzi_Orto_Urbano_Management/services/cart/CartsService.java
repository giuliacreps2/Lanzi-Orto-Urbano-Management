package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartsService implements ICartService {

    private final CartRepository cartRepository;


    @Override
    public Cart getActiveCartByUserId(User currentUser) {
        if (currentUser.getB2cProfile() != null) {
            Cart newCart = Cart.builder()
                    .b2cProfile(currentUser.getB2cProfile())
                    .cartCreatedAt(LocalDateTime.now())
                    .cartStatus(CartStatus.OPEN)
                    .build();
            return cartRepository.save(newCart);
        } else if (currentUser.getB2bProfile() != null) {
            Cart newCartB2b = Cart.builder()
                    .b2bProfile(currentUser.getB2bProfile())
                    .cartCreatedAt(LocalDateTime.now())
                    .cartStatus(CartStatus.OPEN)
                    .build();
            return cartRepository.save(newCartB2b);
        }
        return null;
    }

    @Override
    public Cart findById(UUID cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }

    @Override
    public Cart refreshCartTotal(Cart cart) {
        return null;
    }

    @Override
    public void clearCart(UUID cartId) {

    }

    @Override
    public void markCartAsConverted(Cart cart, Order order) {

    }
}
