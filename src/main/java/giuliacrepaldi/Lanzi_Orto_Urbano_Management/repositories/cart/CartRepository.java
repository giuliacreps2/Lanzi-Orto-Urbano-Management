package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findByUserId(User currentUser);

    void deleteByCartId(Cart found);

    Optional<Cart> findByB2cProfileAndCartStatus(B2cProfile b2cProfile, CartStatus cartStatus);

    Optional<Cart> findByB2bProfileAndCartStatus(B2bProfile b2bProfile, CartStatus cartStatus);

    Optional<Cart> findByEmailWithoutAuthUserAndCartStatus(String email, CartStatus status);
}
