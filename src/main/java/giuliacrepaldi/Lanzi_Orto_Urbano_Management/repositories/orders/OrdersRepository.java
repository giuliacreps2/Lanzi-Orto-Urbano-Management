package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Order, UUID> {
    List<Order> findByB2bProfile_B2bProfileIdOrderByOrderCreatedAtDesc(UUID b2bProfileId);

    List<Order> findByB2cProfile_B2cProfileIdOrderByOrderCreatedAtDesc(UUID b2cProfileId);


}
