package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProvincesRepository extends JpaRepository<Province, UUID> {
    Optional<Province> findById(UUID provinceId);

    Optional<Province> findByProvinceName(String provinceName);

    Optional<Province> findByAcronym(String acronym);
}
