package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface B2bProfilesRepository extends JpaRepository<B2bProfile, UUID> {
    boolean existsByVatNumber(String vatNumber);

    boolean existsByFiscalCode(String fiscalCode);

    B2bProfile findByVatNumber(String vatNumber);

    Optional<B2bProfile> findByUser_UserId(UUID userId);

    B2bProfile findByFiscalCode(String fiscalCode);

    Page<B2bProfile> findAll(Specification<B2bProfile> spec, Pageable pageable);

    boolean existsByContactEmail(String contactEmail);
}
