package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCategoryDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductCategoriesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class ProductCategoriesController {

    private final ProductCategoriesService productCategoriesService;

    public ProductCategoriesController(ProductCategoriesService productCategoriesService) {
        this.productCategoriesService = productCategoriesService;
    }

    //POST
    @PostMapping("/new-cat")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategory saveProductCategory(@RequestBody @Validated ProductCategoryDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.productCategoriesService.saveProdCategory(body);
    }


    //GET
    @GetMapping("/{productCategoryId}")
    public ProductCategory findById(@PathVariable UUID productCategoryId) {
        return this.productCategoriesService.findById(productCategoryId);
    }

    @GetMapping
    public Page<ProductCategory> findAll(Integer page, Integer size, String sortBy) {
        if (page == null) page = 0;
        if (size == null) size = 10;
        if (sortBy == null) sortBy = "productCategoryId";

        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return productCategoriesService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{productCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategory update(@PathVariable UUID productCategoryId, @RequestBody @Validated ProductCategoryDTO body) {
        return this.productCategoriesService.findByIdAndUpdate(productCategoryId, body);
    }

    @PatchMapping("/{productCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductCategory> patchCategory(@PathVariable UUID productCategoryId, @RequestBody Map<String, Object> updates) {
        ProductCategory productCategoryUpdated = this.productCategoriesService.patchProdCategory(productCategoryId, updates);
        return ResponseEntity.ok(productCategoryUpdated);
    }


    //SOFT DELETE
    @DeleteMapping("/{productCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> softDelete(@PathVariable UUID productCategoryId) {
        this.productCategoriesService.deleteProdCategory(productCategoryId);
        return ResponseEntity.noContent().build();
    }

    //DELETE
    @DeleteMapping("/delete/{productCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID productCategoryId) {
        this.productCategoriesService.deleteProdCategory(productCategoryId);
    }

}
