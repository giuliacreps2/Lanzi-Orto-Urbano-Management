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
    @PreAuthorize("hasAthority('ADMIN')")
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
    public ProductVariant findById(UUID productVariantId) {
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

    //DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductVariant(UUID productVariantId) {
        this.productVariantsService.deleteProductVariantById(productVariantId);
    }

}
