package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartItemRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartsService implements ICartService {

    private final CartItemRepository cartItemRepository;
    private final OrdersRepository ordersRepository;
    private final CartsRepository cartsRepository;

    @Override
    public Cart getActiveCartByUserId(User currentUser, CartStatus cartStatus) {
        if (currentUser.getB2cProfile() != null) {
            return cartsRepository.findByB2cProfileAndCartStatus(currentUser.getB2cProfile(), cartStatus)
                    .orElseGet(() -> cartsRepository.save(Cart.builder()
                            .b2cProfile(currentUser.getB2cProfile())
                            .cartCreatedAt(LocalDateTime.now())
                            .cartLastActivityAt(LocalDateTime.now())
                            .cartStatus(CartStatus.OPEN)
                            .build()));
        } else if (currentUser.getB2bProfile() != null) {
            return cartsRepository.findByB2bProfileAndCartStatus(currentUser.getB2bProfile(), cartStatus)
                    .orElseGet(() -> cartsRepository.save(Cart.builder()
                            .b2bProfile(currentUser.getB2bProfile())
                            .cartCreatedAt(LocalDateTime.now())
                            .cartLastActivityAt(LocalDateTime.now())
                            .cartStatus(CartStatus.OPEN)
                            .build()));
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Cart getActiveCartByUserId(User currentUser) {
        return null;
    }

    @Override
    public Cart findById(UUID cartId) {
        return cartsRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Not found cart with this id: " + cartId));
    }

    @Override
    public Cart refreshCartTotal(Cart cart) {
        cart.setCartLastActivityAt(LocalDateTime.now());
        return cartsRepository.save(cart);
    }

    @Override
    public void clearCart(UUID cartId) {
        Cart found = cartsRepository.findById(cartId).orElse(null);
        if (found != null) {
            cartItemRepository.deleteAllByCart_CartId(cartId);
            found.getItems().forEach(i -> i.setCart(null));
            cartsRepository.deleteByCartId(cartId);
        }
    }

    @Override
    public void markCartAsConverted(Cart cart, Order order) {
        cart.setCartStatus(CartStatus.CONVERTED);
        cart.setConvertedOrder(order);
        cart.setCartLastActivityAt(LocalDateTime.now());
        cartsRepository.save(cart);
    }

    @Override
    public Cart getActiveCartByEmail(String email) {
        return null;
    }

    @Override
    public Cart getActiveCartByEmail(String email, CartStatus cartStatus) {
        return cartsRepository.findByEmailWithoutAuthUserAndCartStatus(email, cartStatus)
                .orElseGet(() -> cartsRepository.save(Cart.builder()
                        .emailWithoutAuthUser(email)
                        .cartCreatedAt(LocalDateTime.now())
                        .cartLastActivityAt(LocalDateTime.now())
                        .cartStatus(CartStatus.OPEN)
                        .build()));
    }

    @Override
    public Cart getCartForCheckout(UUID cartId, User currentUser, String guestEmail) {

        if (currentUser == null) {

            if (guestEmail == null || guestEmail.isBlank()) {
                throw new BadRequestException("Guest email is required");
            }

            return cartsRepository.findByCartIdAndEmailWithoutAuthUser(cartId, guestEmail)
                    .orElseThrow(() -> new NotFoundException("Cart not found"));
        }

        if (currentUser.getB2cProfile() != null) {

            Optional<Cart> b2cCart = cartsRepository.findByCartIdAndB2cProfile(cartId, currentUser.getB2cProfile());

            if (b2cCart.isPresent()) {
                return b2cCart.get();
            }
        }

        if (currentUser.getB2bProfile() != null) {

            Optional<Cart> b2bCart = cartsRepository.findByCartIdAndB2bProfile(cartId, currentUser.getB2bProfile());

            if (b2bCart.isPresent()) {
                return b2bCart.get();
            }
        }


        throw new NotFoundException("Cart not found");
    }
}
