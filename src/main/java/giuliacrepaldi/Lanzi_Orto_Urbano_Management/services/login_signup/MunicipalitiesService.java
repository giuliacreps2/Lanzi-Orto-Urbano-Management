package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Municipality;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.MunicipalitiesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipalitiesService {

    private final MunicipalitiesRepository municipalitiesRepository;

    public MunicipalitiesService(MunicipalitiesRepository municipalitiesRepository) {
        this.municipalitiesRepository = municipalitiesRepository;
    }

    public Page<Municipality> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.municipalitiesRepository.findAll(pageable);
    }


    public List<Municipality> findByMunicipalityName(String municipalityName) {
        return this.municipalitiesRepository.findByMunicipalityNameContainingIgnoreCase(municipalityName);
    }
}
