package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface RegistrationRequestsRepository extends JpaRepository<RegistrationRequest, UUID> {
    boolean existsById(UUID registrationRequestId);

    boolean existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(String email, LocalDateTime now);
}
