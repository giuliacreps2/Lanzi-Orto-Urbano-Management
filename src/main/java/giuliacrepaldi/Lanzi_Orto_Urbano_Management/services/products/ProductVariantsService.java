package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductVariantDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductVariantsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProductVariantsService {

    private final ProductVariantsRepository productVariantsRepository;
    private final PriceListsService priceListsService;
    private final PackagingTypesService packagingTypesService;
    private final ProductsService productsService;

    public ProductVariantsService(@Lazy ProductVariantsRepository productVariantsRepository, @Lazy PriceListsService priceListsService, @Lazy PackagingTypesService packagingTypesService, ProductsService productsService) {
        this.productVariantsRepository = productVariantsRepository;
        this.priceListsService = priceListsService;
        this.packagingTypesService = packagingTypesService;
        this.productsService = productsService;
    }

    //CREATE
    public ProductVariant saveNewProductVariant(ProductVariantDTO body) {

        ProductVariant newProductVariant = ProductVariant.builder()
                .skuVariant(body.skuVariant())
                .activeVariant(body.activeVariant())
                .netWeight(body.netWeight())
                .unit(body.unit())
                .product(productsService.findById(body.productId()))
                .packagingType(packagingTypesService.findById(body.packTypeId()))
                .build();

        ProductVariant savedProductVariant = productVariantsRepository.save(newProductVariant);
        log.info("Product Variant saved successfully, {}", savedProductVariant);

        return savedProductVariant;
    }

    //REQUESTS
    public ProductVariant findById(UUID variantId) {
        return this.productVariantsRepository.findById(variantId).orElseThrow(() -> new NotFoundException("Product Variant not found"));
    }

    public Page<ProductVariant> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.productVariantsRepository.findAll(pageable);
    }

    public void validateVariantData(ProductVariant variant) {
        if (variant.getUnit().isMeasurable() && variant.getNetWeight() == null) {
            throw new IllegalArgumentException("About Unit" + variant.getUnit() + "is required net weight");
        }
        if (!variant.getUnit().isMeasurable() && variant.getNetWeight() == null) {
            log.info("About this product variant net weight is not required");
        }
    }

    //UPDATE

    public ProductVariant findByIdAndUpdateProductVariant(UUID variantId, ProductVariantDTO body) {
        if (!productVariantsRepository.existsById(variantId)) throw new NotFoundException("Product Variant not found");

        ProductVariant found = this.findById(variantId);

        found.setSkuVariant(body.skuVariant());
        found.setActiveVariant(body.activeVariant());
        found.setNetWeight(body.netWeight());
        found.setUnit(body.unit());
        found.setPackagingType(packagingTypesService.findById(body.packTypeId()));

        ProductVariant updated = this.productVariantsRepository.save(found);
        log.info("Product Variant updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deleteProductVariantById(UUID variantId) {
        if (!productVariantsRepository.existsById(variantId)) throw new NotFoundException("Product Variant not found");
        log.info("Product deleted successfully, productId: {}", variantId);
        productVariantsRepository.deleteById(variantId);
    }
}
