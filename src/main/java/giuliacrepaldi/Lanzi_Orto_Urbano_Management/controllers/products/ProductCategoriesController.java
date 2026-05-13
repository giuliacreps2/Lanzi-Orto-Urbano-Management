package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCategoryDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductCategoriesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prod")
public class ProductCategoriesController {

    private final ProductCategoriesService productCategoriesService;

    public ProductCategoriesController(ProductCategoriesService productCategoriesService) {
        this.productCategoriesService = productCategoriesService;
    }

    //POST
    @PostMapping("/category")
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
    public ProductCategory findById(UUID productCategoryId) {
        return this.productCategoriesService.findById(productCategoryId);
    }

    @GetMapping
    public Page<ProductCategory> findAll(int page, int size, String sortBy) {
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

    //DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(UUID productCategoryId) {
        this.productCategoriesService.deleteProdCategory(productCategoryId);
    }

}
