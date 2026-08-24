package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartItemRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartsService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrdersRepository ordersRepository;

    @Override
    public Cart getActiveCartByUserId(User currentUser) {
        if (currentUser.getB2cProfile() != null) {
            return cartRepository.findByB2cProfileAndCartStatus(currentUser.getB2cProfile(), CartStatus.OPEN)
                    .orElseGet(() -> cartRepository.save(Cart.builder()
                            .b2cProfile(currentUser.getB2cProfile())
                            .cartCreatedAt(LocalDateTime.now())
                            .cartLastActivityAt(LocalDateTime.now())
                            .cartStatus(CartStatus.OPEN)
                            .build()));
        } else if (currentUser.getB2bProfile() != null) {
            return cartRepository.findByB2bProfileAndCartStatus(currentUser.getB2bProfile(), CartStatus.OPEN)
                    .orElseGet(() -> cartRepository.save(Cart.builder()
                            .b2bProfile(currentUser.getB2bProfile())
                            .cartCreatedAt(LocalDateTime.now())
                            .cartLastActivityAt(LocalDateTime.now())
                            .cartStatus(CartStatus.OPEN)
                            .build()));
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Cart findById(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Not found cart with this id: " + cartId));
    }

    @Override
    public Cart refreshCartTotal(Cart cart) {
        cart.setCartLastActivityAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(UUID cartId) {
        Cart found = cartRepository.findById(cartId).orElse(null);
        if (found != null) {
            cartItemRepository.deleteAllByCartId(cartId);
            found.getItems().forEach(i -> i.setCart(null));
            cartRepository.deleteByCartId(found);
        }
    }

    @Override
    public void markCartAsConverted(Cart cart, Order order) {
        cart.setCartStatus(CartStatus.CONVERTED);
        cart.setConvertedOrder(order);
        cart.setCartLastActivityAt(LocalDateTime.now());
        cartRepository.save(cart);
    }
}
