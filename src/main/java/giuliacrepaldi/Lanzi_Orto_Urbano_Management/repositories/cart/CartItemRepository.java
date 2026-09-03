package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    void deleteAllByCart_CartId(UUID cartId);


}
