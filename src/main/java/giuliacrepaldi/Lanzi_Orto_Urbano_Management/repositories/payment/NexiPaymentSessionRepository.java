package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment.NexiPaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NexiPaymentSessionRepository extends JpaRepository<NexiPaymentSession, UUID> {
}
