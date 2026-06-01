package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscription, UUID> {
}
