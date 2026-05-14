package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategoryAttribute;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCategoryAttributeDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductCategoryAttributesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category-attribute")
public class ProductCategoryAttributesController {

    private final ProductCategoryAttributesService productCategoryAttributesService;

    public ProductCategoryAttributesController(ProductCategoryAttributesService productCategoryAttributesService) {
        this.productCategoryAttributesService = productCategoryAttributesService;
    }

    //POST
    @PostMapping("/new-attr")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategoryAttribute createProdCatAttribute(@RequestBody @Validated ProductCategoryAttributeDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.productCategoryAttributesService.saveNewProdCategoryAttribute(body);
    }


    //GET
    @GetMapping("/{productCategoryAttributeId}")
    public ProductCategoryAttribute findById(UUID productCategoryAttributeId) {
        return this.productCategoryAttributesService.findById(productCategoryAttributeId);
    }

    @GetMapping
    public Page<ProductCategoryAttribute> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.productCategoryAttributesService.findAll(page, size, sortBy);
    }

    //UPDATE

    @PutMapping("/{productCategoryAttributeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategoryAttribute updateProdCatAttribute(@PathVariable UUID productCategoryAttributeId, @RequestBody @Validated ProductCategoryAttributeDTO body) {
        return this.productCategoryAttributesService.findByIdAndUpdateProdCategoryAttribute(productCategoryAttributeId, body);
    }


    //DELETE
    @DeleteMapping("/{productCategoryAttributeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProdCatAttribute(UUID productCategoryAttributeId) {
        this.productCategoryAttributesService.deleteProductById(productCategoryAttributeId);
    }

}
