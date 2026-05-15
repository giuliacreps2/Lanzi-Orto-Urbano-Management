package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.LabelDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.LabelsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities.BarcodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/labels")
public class LabelsController {

    private final LabelsService labelsService;
    @Autowired
    private BarcodeGenerator barcodeGenerator;

    public LabelsController(LabelsService labelsService) {
        this.labelsService = labelsService;
    }


    //POST
    @PostMapping("/new-lab")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody @Validated LabelDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.labelsService.saveNewLabel(body);
    }

    //GET
    @GetMapping("/{labelId}")
    public Label findById(UUID labelId) {
        return this.labelsService.findById(labelId);
    }

    @GetMapping
    public Page<Label> findAllLabels(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.labelsService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{labelId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Label updateLabel(@PathVariable UUID labelId, @RequestBody @Validated LabelDTO body) {
        return this.labelsService.findByIdAndUpdateLabel(labelId, body);
    }

    //DELETE
    @DeleteMapping("/{labelId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID labelId) {
        this.labelsService.deleteLabelById(labelId);
    }


    //STAMPA ETICHETTE
    @GetMapping(value = "/{labelId}/label", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getLabel(@PathVariable UUID labelId) throws Exception {
        Label label = this.labelsService.findById(labelId);
        BufferedImage writer = barcodeGenerator.generateBarcode128(label.getBarCodeGs1());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(writer, "png", out);

        return out.toByteArray();
    }
}
