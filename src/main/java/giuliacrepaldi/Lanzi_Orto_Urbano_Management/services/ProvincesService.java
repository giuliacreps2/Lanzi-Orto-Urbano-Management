package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Province;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.ProvincesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProvincesService {
    private final ProvincesRepository provincesRepository;

    public ProvincesService(ProvincesRepository provincesRepository) {
        this.provincesRepository = provincesRepository;
    }

    public Page<Province> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.provincesRepository.findAll(pageable);
    }

}
