package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface B2bProfilesRepository extends JpaRepository<B2bProfile, UUID> {
    boolean existsByVatNumber(String vatNumber);

    boolean existsByFiscalCode(String fiscalCode);

    Optional<B2bProfile> findByVatNumber(String vatNumber);
}
