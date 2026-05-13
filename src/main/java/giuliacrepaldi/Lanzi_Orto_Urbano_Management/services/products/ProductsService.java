package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategoryAttribute;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductCategoryAttributesRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductCategoryAttributesRepository productCategoryAttributesRepository;

    public ProductsService(ProductsRepository productsRepository, ProductCategoryAttributesRepository productCategoryAttributesRepository) {
        this.productsRepository = productsRepository;
        this.productCategoryAttributesRepository = productCategoryAttributesRepository;
    }

    //CREATE
    public Product saveNewProduct(ProductDTO body) {

        Product newProduct = Product.builder()
                .productName(body.productName())
                .productSlug(body.productSlug())
                .productDescription(body.productDescription())
                .shortProductDescription(body.shortProductDescription())
                .availabilityStatus(body.availabilityStatus())
                .productIsAvailable(body.productIsAvailable())
                .createdAt(body.createdAt())
                .productCategory(body.productCategory())
                .build();

        Product savedProduct = productsRepository.save(newProduct);
        log.info("Product saved successfully, {}", savedProduct);

        return this.productsRepository.save(savedProduct);
    }

    //REQUESTS
    public Product findById(UUID productId) {
        return this.productsRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Page<Product> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.productsRepository.findAll(pageable);
    }

    //UPDATE

    public Product findByIdAndUpdateProduct(UUID productId, ProductDTO body) {
        if (!productsRepository.existsById(productId)) throw new NotFoundException("Product not found");

        Product found = this.findById(productId);

        found.setProductName(body.productName());
        found.setProductSlug(body.productSlug());
        found.setProductDescription(body.productDescription());
        found.setShortProductDescription(body.shortProductDescription());
        found.setAvailabilityStatus(body.availabilityStatus());
        found.setProductIsAvailable(body.productIsAvailable());
        found.setTechnicalProdDetails(body.technicalProdDetails());
        found.setCreatedAt(body.createdAt());
        found.setProductCategory(body.productCategory());

        Product updated = this.productsRepository.save(found);
        log.info("Product updated successfully, {}", updated);
        return updated;
    }


    //VALIDATION METADATA
    private void validateProductAttribute(Product product) {
        List<ProductCategoryAttribute> schema =
                productCategoryAttributesRepository.findByProductCategory(product.getProductCategory());

        Map<String, Object> values = product.getTechnicalProdDetails();

        for (ProductCategoryAttribute attr : schema) {
            if (attr.isRequired() && !values.containsKey(attr.getProdCatAttributeKey())) {
                throw new IllegalArgumentException("Product category attribute " + attr.getProdCatAttributeKey() + " is required");
            }
        }
    }

    //DELETE
    public void deleteProductById(UUID productId) {
        if (!productsRepository.existsById(productId)) throw new NotFoundException("Product not found");
        log.info("Product deleted successfully, productId: {}", productId);
        productsRepository.deleteById(productId);
    }
}
