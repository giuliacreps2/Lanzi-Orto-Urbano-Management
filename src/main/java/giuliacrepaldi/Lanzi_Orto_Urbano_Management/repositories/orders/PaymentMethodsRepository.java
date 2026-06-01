package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethod, UUID> {
}
