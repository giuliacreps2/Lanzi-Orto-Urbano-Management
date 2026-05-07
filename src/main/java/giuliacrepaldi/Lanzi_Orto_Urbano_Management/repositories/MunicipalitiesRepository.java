package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MunicipalitiesRepository extends JpaRepository<Municipality, UUID> {
    List<Municipality> findByMunicipalityNameContainingIgnoreCase(String municipalityName);

    List<Municipality> findByMunicipalityNameIgnoreCase(String municipalityName);
}
