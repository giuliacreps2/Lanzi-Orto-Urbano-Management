package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.LabelDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.LabelsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class LabelsService {

    private final LabelsRepository labelsRepository;

    public LabelsService(LabelsRepository labelsRepository) {
        this.labelsRepository = labelsRepository;
    }


    //CREATE
    public Label saveNewLabel(LabelDTO body) {

        Label newLabel = Label.builder()
                .barCodeGs1(body.barCodeGs1())
                .barcodeData(body.barcodeData())
                .productionDate(body.productionDate())
                .bestBeforeDate(body.bestBeforeDate())
                .exitDate(body.exitDate())
                .printedAt(body.printedAt())
                .inventoryDecremented(body.inventoryDecremented())
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
        found.setBatch(body.batch());
        found.setProductVariant(body.productVariant());

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

}
