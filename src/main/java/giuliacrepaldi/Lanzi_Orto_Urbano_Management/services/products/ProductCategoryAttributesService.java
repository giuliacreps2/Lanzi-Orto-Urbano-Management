package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategoryAttribute;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCategoryAttributeDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductCategoryAttributesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductCategoryAttributesService {

    private final ProductCategoryAttributesRepository productCategoryAttributesRepository;
    private final ProductCategoriesService productCategoriesService;

    public ProductCategoryAttributesService(ProductCategoryAttributesRepository productCategoryAttributesRepository, ProductCategoriesService productCategoriesService) {
        this.productCategoryAttributesRepository = productCategoryAttributesRepository;
        this.productCategoriesService = productCategoriesService;
    }

    //CREATE
    public ProductCategoryAttribute saveNewProdCategoryAttribute(ProductCategoryAttributeDTO body) {

        ProductCategoryAttribute newProdCategoryAttribute = ProductCategoryAttribute.builder()
                .prodCatAttributeKey(body.prodCatAttributeKey())
                .prodCatAttributeLabel(body.prodCatAttributeLabel())
                .attrType(body.attrType())
                .required(body.required())
                .defaultValue(body.defaultValue())
                .minValue(body.minValue())
                .maxValue(body.maxValue())
                .build();

        ProductCategoryAttribute savedProdCategoryAttribute = productCategoryAttributesRepository.save(newProdCategoryAttribute);
        log.info("Product Category Attribute saved successfully, {}", savedProdCategoryAttribute);

        return savedProdCategoryAttribute;
    }

    //REQUESTS
    public ProductCategoryAttribute findById(UUID prodCatAttributeId) {
        return this.productCategoryAttributesRepository.findById(prodCatAttributeId).orElseThrow(() -> new NotFoundException("Product Category Attribute not found"));
    }

    public Page<ProductCategoryAttribute> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.productCategoryAttributesRepository.findAll(pageable);
    }

    //UPDATE

    public ProductCategoryAttribute findByIdAndUpdateProdCategoryAttribute(UUID prodCatAttributeId, ProductCategoryAttributeDTO body) {
        if (!productCategoryAttributesRepository.existsById(prodCatAttributeId))
            throw new NotFoundException("Product Category Attribute not found");

        ProductCategoryAttribute found = this.findById(prodCatAttributeId);

        found.setProdCatAttributeKey(body.prodCatAttributeKey());
        found.setProdCatAttributeLabel(body.prodCatAttributeLabel());
        found.setAttrType(body.attrType());
        found.setRequired(body.required());
        found.setDefaultValue(body.defaultValue());
        found.setMinValue(body.minValue());
        found.setMaxValue(body.maxValue());
        found.setUnit(body.unit());
        found.setProductCategory(productCategoriesService.findById(body.productCategoryId()));

        ProductCategoryAttribute updated = this.productCategoryAttributesRepository.save(found);
        log.info("Product Category Attribute updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deleteProductById(UUID prodCatAttributeId) {
        if (!productCategoryAttributesRepository.existsById(prodCatAttributeId))
            throw new NotFoundException("Product not found");
        log.info("Product Category Attribute deleted successfully: {}", prodCatAttributeId);
        productCategoryAttributesRepository.deleteById(prodCatAttributeId);
    }
}

