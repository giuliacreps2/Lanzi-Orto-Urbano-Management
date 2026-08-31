package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreateHostedOrdersReqRepository extends JpaRepository<CreateHostedOrdersReqRepository, UUID> {
}
