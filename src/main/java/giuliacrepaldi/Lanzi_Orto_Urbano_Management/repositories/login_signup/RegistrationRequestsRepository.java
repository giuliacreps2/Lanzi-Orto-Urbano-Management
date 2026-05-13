package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRequestsRepository extends JpaRepository<RegistrationRequest, UUID> {
    boolean existsById(UUID registrationRequestId);

    boolean existsByEmailAndIsUsedFalseAndTokenExpiresAtAfter(String email, LocalDateTime now);

    Optional<RegistrationRequest> findByVerificationToken(String token);
}
