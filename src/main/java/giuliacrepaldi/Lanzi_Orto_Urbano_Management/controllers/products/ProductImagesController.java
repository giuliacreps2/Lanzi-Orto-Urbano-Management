package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductImage;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductImageDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductImagesServices;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ProductImagesController {

    private final ProductImagesServices productImagesServices;

    public ProductImagesController(ProductImagesServices productImagesServices) {
        this.productImagesServices = productImagesServices;
    }

    //POST
    @PostMapping("/new-img")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductImage createProductImage(@RequestBody @Validated ProductImageDTO body, MultipartFile file, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.productImagesServices.saveImage(file, body.productId(), body.altText(), body.sortOrder(), body.isPrimary());
    }


    //GET
    @GetMapping("/{prodImageId}")
    public ProductImage findById(@PathVariable UUID prodImageId) {
        return this.productImagesServices.findById(prodImageId);
    }

    @GetMapping
    public Page<ProductImage> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.productImagesServices.findAll(page, size, sortBy);
    }


    //PUT
    //PATCH


    //DELETE
    @DeleteMapping("/{prodImageId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID prodImageId) {
        this.productImagesServices.delete(prodImageId);
    }


}
