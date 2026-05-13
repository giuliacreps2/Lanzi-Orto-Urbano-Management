package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface B2cProfilesRepository extends JpaRepository<B2cProfile, UUID> {

    Optional<B2cProfile> findByUser_UserId(UUID userId);
}
