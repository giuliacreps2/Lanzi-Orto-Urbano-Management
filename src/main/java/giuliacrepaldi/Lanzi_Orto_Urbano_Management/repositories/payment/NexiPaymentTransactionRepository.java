package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment.NexiPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NexiPaymentTransactionRepository extends JpaRepository<NexiPaymentTransaction, UUID> {
}
