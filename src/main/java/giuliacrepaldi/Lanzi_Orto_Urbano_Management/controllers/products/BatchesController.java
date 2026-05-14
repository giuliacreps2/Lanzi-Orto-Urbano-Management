package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.BatchDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.BatchesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/batches")
public class BatchesController {

    private final BatchesService batchesService;

    public BatchesController(BatchesService batchesService) {
        this.batchesService = batchesService;
    }

    //POST
    @PostMapping("/new-batch")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Batch createBatch(@RequestBody @Validated BatchDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.batchesService.saveNewBatch(body);
    }


    //GET
    @GetMapping("/{batchId}")
    public Batch findById(UUID batchId) {
        return this.batchesService.findById(batchId);
    }

    @GetMapping
    public Page<Batch> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.batchesService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{batchId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Batch update(@PathVariable UUID batchId, @RequestBody @Validated BatchDTO body) {
        return this.batchesService.findByIdAndUpdateBAtch(batchId, body);
    }


    //DELETE
    @DeleteMapping("/{batchId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteById(@PathVariable UUID batchId) {
        this.batchesService.deleteBatchById(batchId);
    }
}
