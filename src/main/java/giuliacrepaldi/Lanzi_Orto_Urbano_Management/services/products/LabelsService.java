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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        //AUTOMAZIONE GS1
        String generatedGs1Code = generateGs1String(productVariant.getSkuVariant(), body.productionDate(), batch.getBatchCode());

        Label newLabel = Label.builder()
                .barCodeGs1(generatedGs1Code)
                .barCodeGs1(body.barCodeGs1())
                .barcodeData(body.barcodeData())
                .productionDate(body.productionDate())
                .bestBeforeDate(body.bestBeforeDate())
                .exitDate(body.exitDate())
                .printedAt(body.printedAt())
                .inventoryDecremented(false)
                .batch(batch)
                .productVariant(productVariant)
                .build();

        Label savedLabel = labelsRepository.save(newLabel);
        log.info("Label saved successfully, {}", savedLabel);

        return savedLabel;
    }

    private String generateGs1String(String skuVariant, LocalDate prodDate, String batchCode) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = prodDate.format(formatter);

        //COSTRUZIONE STRINGA: 01 = SKU, 11 = DATA PROD, 10 = LOTTO
        return "01" + skuVariant + "11" + dateString + "10" + batchCode;
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

        if (!found.getBatch().getBatchId().equals(body.batchId())) {
            found.setBatch(batchesService.findById(body.batchId()));
        }

        if (!found.getProductVariant().getVariantId().equals(body.productVariantId())) {
            found.setProductVariant(productVariantsService.findById(body.productVariantId()));
        }

        found.setBarcodeData(body.barcodeData());
        found.setProductionDate(body.productionDate());
        found.setBestBeforeDate(body.bestBeforeDate());
        found.setExitDate(body.exitDate());
        found.setPrintedAt(body.printedAt());
        found.setInventoryDecremented(body.inventoryDecremented());

        //RICALCOLO CODICE, SE CAMVìBIANO I DATI
        found.setBarCodeGs1(generateGs1String(found.getProductVariant().getSkuVariant(), found.getProductionDate(), found.getBatch().getBatchCode()));

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
    @Transactional
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
