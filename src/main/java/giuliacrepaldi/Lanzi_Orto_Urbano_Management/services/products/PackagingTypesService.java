package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PackagingType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.PackagingTypeDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.PackagingTypesRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PackagingTypesService {

    private final PackagingTypesRepository packagingTypesRepository;

    public PackagingTypesService(PackagingTypesRepository packagingTypesRepository) {
        this.packagingTypesRepository = packagingTypesRepository;
    }

    //CREATE
    public PackagingType savePackagingType(PackagingTypeDTO body) {
    }


    //FIND
    public PackagingType findById(UUID id) {
    }


    public Page<PackagingType> findAll() {
    }

    //UPDATE
    public PackagingType findByIdAndUpdate(UUID id, PackagingTypeDTO body) {
    }

    //DELETE
    public void deleteById(UUID id) {
    }
}
