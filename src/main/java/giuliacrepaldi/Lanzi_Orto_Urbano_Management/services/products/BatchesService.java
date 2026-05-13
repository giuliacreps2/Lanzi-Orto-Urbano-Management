package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.BatchDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.BatchesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BatchesService {

    private final BatchesRepository batchesRepository;

    public BatchesService(BatchesRepository batchesRepository) {
        this.batchesRepository = batchesRepository;
    }


    //CREATE
    public Batch saveNewBatch(BatchDTO body) {

        Batch newBatch = Batch.builder()
                .batchCode(body.batchCode())
                .statusBatch(body.statusBatch())
                .quantityPlanned(body.quantityPlanned())
                .quantityActual(body.quantityActual())
                .startedAt(body.startedAt())
                .expectedHarvestDate(body.expectedHarvestDate())
                .actualHarvestDate(body.actualHarvestDate())
                .batchMetadata(body.batchMetadata())
                .build();

        Batch savedBatch = batchesRepository.save(newBatch);
        log.info("Batch saved successfully, {}", savedBatch);

        return savedBatch;
    }

    //REQUESTS
    public Batch findById(UUID batchId) {
        return this.batchesRepository.findById(batchId).orElseThrow(() -> new NotFoundException("Batch not found"));
    }

    public Page<Batch> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.batchesRepository.findAll(pageable);
    }

    //UPDATE

    public Batch findByIdAndUpdateBAtch(UUID batchId, BatchDTO body) {
        if (!batchesRepository.existsById(batchId)) throw new NotFoundException("Label not found");

        Batch found = this.findById(batchId);

        found.setBatchCode(body.batchCode());
        found.setStatusBatch(body.statusBatch());
        found.setQuantityPlanned(body.quantityPlanned());
        found.setQuantityActual(body.quantityActual());
        found.setStartedAt(body.startedAt());
        found.setExpectedHarvestDate(body.expectedHarvestDate());
        found.setActualHarvestDate(body.actualHarvestDate());
        found.setBatchMetadata(body.batchMetadata());
        found.setProductVariant(body.productVariant());

        Batch updated = this.batchesRepository.save(found);
        log.info("Label updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deleteBatchById(UUID batchId) {
        if (!batchesRepository.existsById(batchId)) throw new NotFoundException("Batch not found");
        log.info("Label deleted successfully, {}", batchId);
        batchesRepository.deleteById(batchId);
    }

}
