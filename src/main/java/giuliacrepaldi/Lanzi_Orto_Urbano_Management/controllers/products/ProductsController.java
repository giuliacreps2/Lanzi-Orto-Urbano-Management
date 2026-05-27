package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCatalogDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductFormDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    //POST
    @PostMapping("/new-prod")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody @Validated ProductDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.productsService.saveNewProduct(body);
    }


//    @PostMapping("/new-compisite")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Product createCompositeProduct(@RequestBody @Validated ProductFormDTO body, BindingResult validation) {
//        if (validation.hasErrors()) {
//            List<String> errors = validation.getFieldErrors()
//                    .stream().map(e -> e.getDefaultMessage()).toList();
//            throw new ValidationException(errors);
//        }
//        return this.productsService.saveCompositeProduct(body);
//    }

    @PostMapping("/new-composite")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createCompositeProduct(
            @RequestBody @Validated ProductFormDTO body,
            BindingResult validation) {

        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage).toList();
            throw new ValidationException(errors);
        }

        Product created = this.productsService.saveCompositeProduct(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //GET
    @GetMapping("/{productId}")
    public Product findById(@PathVariable UUID productId) {
        return this.productsService.findById(productId);
    }

    @GetMapping
    public Page<Product> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.productsService.findAll(page, size, sortBy);
    }
//
//    @GetMapping
//    public Page<Product> findByStatus(String status, int page, int size, String sortBy) {
//        if (size > 100 || size < 0) size = 10;
//        if (page < 0) page = 0;
//        return this.productsService.findByStatus(status, page, size, sortBy);
//    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> patchProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> updates) {
        Product productUpdated = this.productsService.patchProduct(productId, updates);
        return ResponseEntity.ok(productUpdated);
    }


    @GetMapping("/catalog")
    public ResponseEntity<List<ProductCatalogDTO>> getProductCatalog(Authentication authentication) {
        List<ProductCatalogDTO> catalog = this.productsService.getCatalog(authentication);
        return ResponseEntity.ok(catalog);
    }

    //UPDATE
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Product update(@PathVariable UUID productId, @RequestBody @Validated ProductDTO body) {
        return this.productsService.findByIdAndUpdateProduct(productId, body);
    }

//    @PutMapping("/composite/{productId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @ResponseStatus(HttpStatus.OK)
//    public Product updateCompositeProduct(@PathVariable UUID productId, @RequestBody @Validated ProductFormDTO body, BindingResult validation) {
//        if (validation.hasErrors()) {
//            List<String> errors = validation.getFieldErrors()
//                    .stream().map(e -> e.getDefaultMessage()).toList();
//            throw new ValidationException(errors);
//        }
//        return this.productsService.updateCompositeProduct(productId, body);
//    }

    @PutMapping("/composite/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> updateCompositeProduct(@PathVariable UUID productId,
                                                          @RequestBody @Validated ProductFormDTO body,
                                                          BindingResult validation) {

        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage).toList();
            throw new ValidationException(errors);
        }

        Product updated = this.productsService.updateCompositeProduct(productId, body);
        return ResponseEntity.ok(updated);
    }


    //SOFT DELETE
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> softDeleteProduct(@PathVariable UUID productId) {
        this.productsService.softDeleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


    //DELETE
    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID productId) {
        this.productsService.deleteProductById(productId);
    }

}
