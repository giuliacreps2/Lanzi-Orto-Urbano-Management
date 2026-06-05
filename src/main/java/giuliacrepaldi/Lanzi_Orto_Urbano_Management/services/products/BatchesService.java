package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.StatusBatch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.BatchDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.BatchesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BatchesService {

    private final BatchesRepository batchesRepository;
    private final ProductVariantsService productVariantsService;

    public BatchesService(BatchesRepository batchesRepository, ProductVariantsService productVariantsService) {
        this.batchesRepository = batchesRepository;
        this.productVariantsService = productVariantsService;
    }


    //CREATE
    public Batch saveNewBatch(BatchDTO body) {

        ProductVariant variant = this.productVariantsService.findById(body.productVariantId());

        String category = "generic";
        if (variant.getProduct() != null && variant.getProduct().getProductCategory() != null) {
            category = variant.getProduct().getProductCategory().toString();
        }

        String generatedCode = body.batchCode();
        LocalDate expectedHarvest = body.expectedHarvestDate();
        int currentYear = Year.now().getValue();

        Map<String, Object> metadata = body.batchMetadata() != null ? body.batchMetadata() : new HashMap<>();

        switch (category) {
            case "microgreens":
            case "funghi":
                String supplierCode = metadata.getOrDefault("supplier_batch_code", "GENERIC-SUP").toString();
                String productSku = variant.getSkuVariant() != null ? variant.getSkuVariant() : "PROD";
                generatedCode = "LOT-" + productSku + "-" + supplierCode;

                if (expectedHarvest == null) {
                    LocalDate start = body.startedAt() != null ? body.startedAt().toLocalDate() : LocalDate.now();
                    expectedHarvest = start.plusDays(10);
                }
                break;
            case "miele":
                Object hiveId = metadata.getOrDefault("hive_id", "01");
                generatedCode = "ARN-" + hiveId + "-" + currentYear;

                if (expectedHarvest == null) {
                    expectedHarvest = LocalDate.now();
                }
                break;
            case "zafferano":
                String bulbCode = metadata.getOrDefault("bulb_code", "BULB-ZAF").toString();
                generatedCode = "ZAF-" + bulbCode + "-" + currentYear;

                if (expectedHarvest == null) {
                    expectedHarvest = LocalDate.now().plusMonths(4);
                }
                break;

            default:

                if (generatedCode == null || generatedCode.isEmpty()) {
                    generatedCode = "LOT-" + currentYear + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                }
                if (expectedHarvest == null) {
                    expectedHarvest = LocalDate.now().plusDays(7);
                }
                break;
        }

        Batch newBatch = Batch.builder()
                .batchCode(generatedCode)
                .statusBatch(body.statusBatch() != null ? body.statusBatch() : StatusBatch.INCUBATION)
                .batchCreatedAt(LocalDateTime.now())
                .quantityPlanned(body.quantityPlanned())
                .quantityActual(body.quantityActual())
                .startedAt(body.startedAt() != null ? body.startedAt() : LocalDateTime.now())
                .expectedHarvestDate(expectedHarvest)
                .actualHarvestDate(body.actualHarvestDate())
                .batchMetadata(metadata)
                .productVariant(variant)
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

    public Batch findByIdAndUpdateBatch(UUID batchId, BatchDTO body) {
        if (!batchesRepository.existsById(batchId)) throw new NotFoundException("Batch not found");

        Batch found = this.findById(batchId);

        found.setBatchCode(body.batchCode());
        found.setStatusBatch(body.statusBatch());
        found.setQuantityPlanned(body.quantityPlanned());
        found.setQuantityActual(body.quantityActual());
        found.setStartedAt(body.startedAt());
        found.setExpectedHarvestDate(body.expectedHarvestDate());
        found.setActualHarvestDate(body.actualHarvestDate());
        found.setBatchMetadata(body.batchMetadata());
        found.setProductVariant(productVariantsService.findById(body.productVariantId()));

        Batch updated = this.batchesRepository.save(found);
        log.info("Batch updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deleteBatchById(UUID batchId) {
        if (!batchesRepository.existsById(batchId)) throw new NotFoundException("Batch not found");
        batchesRepository.deleteById(batchId);
        log.info("Batch deleted successfully, {}", batchId);
    }

    public Batch findAvailableBatchForVariant(UUID variantId) {
        return null;
    }
}
