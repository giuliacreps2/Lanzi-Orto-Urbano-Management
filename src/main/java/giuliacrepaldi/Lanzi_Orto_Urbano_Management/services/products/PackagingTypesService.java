package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PackagingType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.PackagingTypeDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.PackagingTypesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PackagingTypesService {

    private final PackagingTypesRepository packagingTypesRepository;

    public PackagingTypesService(PackagingTypesRepository packagingTypesRepository) {
        this.packagingTypesRepository = packagingTypesRepository;
    }

    //CREATE
    public PackagingType savePackagingType(PackagingTypeDTO body) {

        PackagingType newPackType = PackagingType.builder()
                .namePackType(body.namePackType())
                .dimensionsJsonb(body.dimensionsJsonb())
                .unitOfMeasure(body.unitOfMeasure())
                .build();

        PackagingType savedPackagingType = packagingTypesRepository.save(newPackType);

        log.info("PackagingType {} saved", savedPackagingType);
        return savedPackagingType;
    }


    //FIND
    public PackagingType findById(UUID id) {
        return this.packagingTypesRepository.findById(id).orElseThrow(() -> new NotFoundException("PackagingType not found"));
    }

    public Page<PackagingType> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.packagingTypesRepository.findAll(pageable);
    }

    //UPDATE
    public PackagingType findByIdAndUpdate(UUID packTypeId, PackagingTypeDTO body) {

        if (!packagingTypesRepository.existsById(packTypeId)) throw new NotFoundException("PackagingType not found");

        PackagingType found = this.findById(packTypeId);

        found.setNamePackType(body.namePackType());
        found.setUnitOfMeasure(body.unitOfMeasure());
        found.setDimensionsJsonb(body.dimensionsJsonb());
//        found.setPackagingCategory(body.packagingCategory());
//        found.setProductVariant(body.productVariant());

        PackagingType updatedPackType = this.packagingTypesRepository.save(found);
        log.info("PackagingType updated successfully");

        return updatedPackType;
    }

    //DELETE
    public void deleteById(UUID packTypeId) {
        if (!packagingTypesRepository.existsById(packTypeId)) throw new NotFoundException("PackagingType not found");

        PackagingType found = this.findById(packTypeId);
        log.info("PackagingType deleted successfully");
        packagingTypesRepository.delete(found);
    }
}
