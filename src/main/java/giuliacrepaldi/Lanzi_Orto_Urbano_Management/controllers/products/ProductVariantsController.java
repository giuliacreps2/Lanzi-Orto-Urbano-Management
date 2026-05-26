package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductVariantDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductVariantsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/variants")
public class ProductVariantsController {

    private final ProductVariantsService productVariantsService;

    public ProductVariantsController(ProductVariantsService productVariantsService) {
        this.productVariantsService = productVariantsService;
    }

    //POST
    @PostMapping("/new-var")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVariant createProductVariant(@RequestBody @Validated ProductVariantDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.productVariantsService.saveNewProductVariant(body);
    }

    //GET
    @GetMapping("/{productVariantId}")
    public ProductVariant findById(@PathVariable UUID productVariantId) {
        return this.productVariantsService.findById(productVariantId);
    }

    @GetMapping
    public Page<ProductVariant> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.productVariantsService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{productVariantId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductVariant updateProductVariant(@PathVariable UUID productVariantId, @RequestBody @Validated ProductVariantDTO body) {
        return this.productVariantsService.findByIdAndUpdateProductVariant(productVariantId, body);
    }

//    @PatchMapping("/{productVariantId}")
//    public ResponseEntity<ProductVariant> patchVariant(@PathVariable UUID productVariantId, @RequestBody Map<String, Object> updates) {
//        ProductVariant productVariantUpdated = this.productVariantsService.patchProductVariant(productVariantId);
//        return ResponseEntity.ok(productVariantUpdated);
//    }


    //SOFT DELETE
//    @DeleteMapping("/{productVariantId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Void> softDeleteProductVariant(@PathVariable UUID productVariantId) {
//        this.productVariantsService.softDeleteProductVariant(productVariantId);
//        return ResponseEntity.noContent().build();
//    }


    //DELETE
    @DeleteMapping("/{productVariantId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductVariant(@PathVariable UUID productVariantId) {
        this.productVariantsService.deleteProductVariantById(productVariantId);
    }

}
