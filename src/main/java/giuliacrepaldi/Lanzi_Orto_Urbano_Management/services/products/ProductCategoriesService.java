package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCategoryDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductCategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProductCategoriesService {

    private final ProductCategoriesRepository productCategoriesRepository;

    public ProductCategoriesService(ProductCategoriesRepository productCategoriesRepository) {
        this.productCategoriesRepository = productCategoriesRepository;
    }

    //SONO TUTTI METODI CHE POSSONO USARE SOLO ADMIN E IL SUO STAFF
    //CREATE
    //Come faccio ad inserire campi personalizzati nel metadata--->direttamente sul json Postman
    public ProductCategory saveProdCategory(ProductCategoryDTO body) {

        ProductCategory newProductCategory = ProductCategory.builder()
                .nameProdCategory(body.nameProdCategory())
                .requiresBatchTracking(body.requiresBatchTracking())
                .metadataProdCategory(body.metadataProdCategory())
                .build();


        ProductCategory savedProductCategory = productCategoriesRepository.save(newProductCategory);

        return savedProductCategory;
    }

    //REQUESTS
    public ProductCategory findById(UUID productCategoryId) {
        return this.productCategoriesRepository.findById(productCategoryId).orElseThrow(() -> new NotFoundException("ProductCategory not found"));
    }

    public Page<ProductCategory> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productCategoriesRepository.findAll(pageable);
    }


    //UPDATE
    public ProductCategory findByIdAndUpdate(UUID productCategoryId, ProductCategoryDTO body) {

        if (!productCategoriesRepository.existsById(productCategoryId))
            throw new NotFoundException("Product Category not found");

        ProductCategory found = findById(productCategoryId);

        found.setNameProdCategory(body.nameProdCategory());
        found.setMetadataProdCategory(body.metadataProdCategory());

        ProductCategory updatedProductCategory = productCategoriesRepository.save(found);

        log.info("Product Category updated successfully");

        return updatedProductCategory;
    }

    //DELETE
    public void deleteProdCategory(UUID productCategoryId) {

        if (!productCategoriesRepository.existsById(productCategoryId))
            throw new NotFoundException("Product Category not found");

        ProductCategory found = findById(productCategoryId);
        log.info("Product Category deleted successfully: {}", found);

        this.productCategoriesRepository.delete(found);
    }
}
