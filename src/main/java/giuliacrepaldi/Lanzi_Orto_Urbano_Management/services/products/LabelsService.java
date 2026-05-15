package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.LabelDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.LabelsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory.InventoryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class LabelsService {

    private final LabelsRepository labelsRepository;
    private final BatchesService batchesService;
    private final ProductVariantsService productVariantsService;
    private final InventoryService inventoryService;


    public LabelsService(LabelsRepository labelsRepository, BatchesService batchesService, ProductVariantsService productVariantsService, InventoryService inventoryService) {
        this.labelsRepository = labelsRepository;
        this.batchesService = batchesService;
        this.productVariantsService = productVariantsService;
        this.inventoryService = inventoryService;
    }


    //CREATE
    @Transactional
    public Label saveNewLabel(LabelDTO body) {

        Batch batch = this.batchesService.findById(body.batchId());
        ProductVariant productVariant = this.productVariantsService.findById(body.productVariantId());

        Label newLabel = Label.builder()
                .barCodeGs1(body.barCodeGs1())
                .barcodeData(body.barcodeData())
                .productionDate(body.productionDate())
                .bestBeforeDate(body.bestBeforeDate())
                .exitDate(body.exitDate())
                .printedAt(body.printedAt())
                .inventoryDecremented(body.inventoryDecremented())
                .batch(batch)
                .productVariant(productVariant)
                .build();

        Label savedLabel = labelsRepository.save(newLabel);
        log.info("Label saved successfully, {}", savedLabel);

        return savedLabel;
    }

    //REQUESTS
    public Label findById(UUID labelId) {
        return this.labelsRepository.findById(labelId).orElseThrow(() -> new NotFoundException("Label not found"));
    }

    public Page<Label> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.labelsRepository.findAll(pageable);
    }

    //UPDATE

    public Label findByIdAndUpdateLabel(UUID labelId, LabelDTO body) {
        if (!labelsRepository.existsById(labelId)) throw new NotFoundException("Label not found");

        Label found = this.findById(labelId);

        found.setBarCodeGs1(body.barCodeGs1());
        found.setBarcodeData(body.barcodeData());
        found.setProductionDate(body.productionDate());
        found.setBestBeforeDate(body.bestBeforeDate());
        found.setExitDate(body.exitDate());
        found.setPrintedAt(body.printedAt());
        found.setInventoryDecremented(body.inventoryDecremented());
        found.setBatch(batchesService.findById(body.batchId()));
        found.setProductVariant(productVariantsService.findById(body.productVariantId()));

        if (!found.getBatch().getBatchId().equals(body.batchId())) {
            found.setBatch(batchesService.findById(body.batchId()));
        }

        Label updated = this.labelsRepository.save(found);
        log.info("Label updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deleteLabelById(UUID labelId) {
        if (!labelsRepository.existsById(labelId)) throw new NotFoundException("Label not found");
        log.info("Label deleted successfully, {}", labelId);
        labelsRepository.deleteById(labelId);
    }


    //ETICHETTE REALI
    public void processLabelScan(UUID labelId) {

        Label found = this.findById(labelId);

        //Controllo etichetta già scansionata
        if (found.isInventoryDecremented()) {
            throw new BadRequestException("This label was already decremented");
        }

        //Recupero la configurazione del JSNOB dalla categoria
        Map<String, Object> categoryMetadata = found.getProductVariant()
                .getProduct()
                .getProductCategory()
                .getMetadataProdCategory();

        //Chiamo il servizio dell'inventario
        inventoryService.processInventoryDecrement(found, categoryMetadata);

        //Definisco il decremento nell'inventario e salvo
        found.setInventoryDecremented(true);
        labelsRepository.save(found);

        log.info("Label scan done successfully, {}", found);
    }


}
