package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.ProductStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCatalogDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductFormDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.PriceListsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductCategoryAttributesRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductVariantsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductCategoryAttributesRepository productCategoryAttributesRepository;
    private final ProductCategoriesService productCategoriesService;
    private final ProductVariantsRepository productVariantsRepository;
    private final PriceListsRepository priceListsRepository;
    private final PackagingTypesService packagingTypesService;
    private final UsersService usersService;

    public ProductsService(ProductsRepository productsRepository, ProductCategoryAttributesRepository productCategoryAttributesRepository, ProductCategoriesService productCategoriesService, ProductVariantsRepository productVariantsRepository, PriceListsRepository priceListsRepository, PackagingTypesService packagingTypesService, UsersService usersService) {
        this.productsRepository = productsRepository;
        this.productCategoryAttributesRepository = productCategoryAttributesRepository;
        this.productCategoriesService = productCategoriesService;
        this.productVariantsRepository = productVariantsRepository;
        this.priceListsRepository = priceListsRepository;
        this.packagingTypesService = packagingTypesService;
        this.usersService = usersService;
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
                .productStatus(body.productStatus())
                .productCategory(productCategoriesService.findById(body.productCategoryId()))
                .build();

        log.info("PRODUCT NAME" + newProduct.getProductName());
        log.info("PRODUCT SLUG" + newProduct.getProductSlug());
        log.info("PRODUCT DESCRIPTION" + newProduct.getProductDescription());
        log.info("PRODUCT AVAILABILITY STATUS" + newProduct.getAvailabilityStatus());
        log.info("PRODUCT IS AVAILABLE" + newProduct.getAvailabilityStatus());
        log.info("PRODUCT CREATED AT" + newProduct.getCreatedAt());

        Product savedProduct = productsRepository.save(newProduct);
        log.info("Product saved successfully, {}", savedProduct);

        return savedProduct;
    }


    @Transactional
    public Product saveCompositeProduct(ProductFormDTO body) {
        ProductCategory category = this.productCategoriesService.findById(body.productCategoryId());

        Product newProduct = Product.builder()
                .productName(body.productName())
                .productSlug(body.productSlug())
                .productDescription(body.productDescription())
                .shortProductDescription(body.shortProductDescription())
                .availabilityStatus(body.availabilityStatus())
                .productIsAvailable(body.productIsAvailable())
                .productStatus(body.productStatus())
                .productCategory(category)
                .build();

        Product savedProduct = productsRepository.save(newProduct);

        PackagingType packType = this.packagingTypesService.findById(body.packTypeId());

        ProductVariant newProdVar = ProductVariant.builder()
                .skuVariant(body.skuVariant())
                .activeVariant(body.activeVariant())
                .netWeight(body.netWeight())
                .unit(body.unit())
                .technicalDetails(body.technicalDetails())
                .product(savedProduct)
                .packagingType(packType)
                .build();

        ProductVariant savedProdVar = productVariantsRepository.save(newProdVar);

        PriceList b2cPrice = PriceList.builder()
                .price(body.b2cPrice())
                .minOrderQuantity(1)
                .clientCategory(ClientCategory.B2C)
                .productVariant(savedProdVar)
                .build();

        PriceList newB2cPrice = priceListsRepository.save(b2cPrice);

        PriceList b2bPrice = PriceList.builder()
                .price(body.b2bPrice())
                .minOrderQuantity(body.b2bMinOrderQuantity() != null ? body.b2bMinOrderQuantity() : 1)
                .clientCategory(ClientCategory.B2B)
                .productVariant(savedProdVar)
                .build();

        PriceList newB2bPrice = priceListsRepository.save(b2bPrice);

        return savedProduct;
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

//    public ClientCategory findClientCategoryByUserId(UUID userId) {
//        return this.productsRepository.findClientCategoryByUserId(userId);
//    }
//


    //RICHIESTA PER CAPIRE SE CLIENTE B2B/B2C
    public ClientCategory resolveClientCategory(User currentUser) {
        if (currentUser == null) {
            return ClientCategory.B2C;
        }
        if (
                currentUser.getB2bProfile() != null && currentUser.getB2bProfile().getStatusB2b() == StatusB2b.APPROVED
        ) {
            return ClientCategory.B2B;
        }
        return ClientCategory.B2C;
    }

    //SWITCH CATALOGO PRODOTTI PER B2C/B2B
    public List<ProductCatalogDTO> getCatalogForUser(User currentUser) {

        ClientCategory clientCategory = resolveClientCategory(currentUser);

        List<ProductVariant> variants = productVariantsRepository.findByActiveVariantTrue();

        log.info("DEBUG CATALOGO - Numero di varianti attive trovate nel DB: {}", variants.size());

        return variants.stream()
                .filter(variant -> variant.getProduct() != null
                        && variant.getProduct().getDeletedAt() == null
                        && variant.getProduct().getProductStatus() == ProductStatus.ACTIVE)

                .map(variant -> {
                    PriceList priceList = variant.getPriceList()
                            .stream()
                            .filter(price -> price.getClientCategory() == clientCategory)
                            .findFirst()
                            .orElseGet(() -> variant.getPriceList()
                                    .stream()
                                    .filter(price -> price.getClientCategory() == ClientCategory.B2C)
                                    .findFirst()
                                    .orElseThrow(() -> new NotFoundException("Nessun listino prezzi (B2C/B2B) trovato per la variante: " + variant.getVariantId()))
                            );

                    Product product = variant.getProduct();

                    String priceLabel = clientCategory == ClientCategory.B2B
                            ? "+IVA"
                            : "IVA inclusa";

                    double safeNetWeight = variant.getNetWeight() != null ? variant.getNetWeight() : 0.0;

                    String safeUnit = variant.getUnit() != null ? variant.getUnit().toString() : "";

                    return new ProductCatalogDTO(
                            product.getProductId(),
                            variant.getVariantId(),
                            product.getProductName(),
                            product.getProductSlug(),
                            product.getShortProductDescription(),
                            variant.getSkuVariant(),
                            safeNetWeight,
                            variant.getUnit().toString(),
                            priceList.getPrice(),
                            clientCategory,
                            priceLabel,
                            priceList.getMinOrderQuantity(),
                            product.isProductIsAvailable(),
                            variant.isActiveVariant()
                    );
                })
                .toList();
    }

    //CATALOGO PRODOTTI UTENTI NON LOGGATI
    public List<ProductCatalogDTO> getCatalog(Authentication authentication) {
        User currentUser = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")
        ) {
            String email = authentication.getName();
            currentUser = this.usersService.findByEmail(email);
        }

        return this.getCatalogForUser(currentUser);
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
//        found.setTechnicalProdDetails(body.technicalProdDetails());
        found.setCreatedAt(body.createdAt());
        found.setProductCategory(productCategoriesService.findById(body.productCategoryId()));

        Product updated = this.productsRepository.save(found);
        log.info("Product updated successfully, {}", updated);
        return updated;
    }


    @Transactional
    public Product updateCompositeProduct(UUID productId, ProductFormDTO body) {
        Product product = this.productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        ProductCategory category = productCategoriesService.findById(body.productCategoryId());

        product.setProductName(body.productName());
        product.setProductSlug(body.productSlug());
        product.setProductDescription(body.productDescription());
        product.setShortProductDescription(body.shortProductDescription()); // ← era productDescription() per errore
        product.setAvailabilityStatus(body.availabilityStatus());
        product.setProductIsAvailable(body.productIsAvailable());
        product.setProductStatus(body.productStatus());
        product.setProductCategory(category);

        Product savedProduct = this.productsRepository.save(product);

        ProductVariant variant = this.productVariantsRepository.findByProductProductId(productId)
                .orElseThrow(() -> new NotFoundException("Product Variant not found"));

        PackagingType packagingType = this.packagingTypesService.findById(body.packTypeId());

        variant.setSkuVariant(body.skuVariant());
        variant.setActiveVariant(body.activeVariant());
        variant.setNetWeight(body.netWeight());
        variant.setUnit(body.unit());
        variant.setTechnicalDetails(body.technicalDetails());
        variant.setPackagingType(packagingType);

        ProductVariant savedVariant = this.productVariantsRepository.save(variant);

        List<PriceList> priceLists = this.priceListsRepository.findByProductVariantId(savedVariant.getVariantId()); // ← usare l'id della variant, non del product

        for (PriceList priceList : priceLists) {
            if (priceList.getClientCategory() == ClientCategory.B2C) {
                priceList.setPrice(body.b2cPrice());
            } else if (priceList.getClientCategory() == ClientCategory.B2B) {
                priceList.setPrice(body.b2bPrice());
                priceList.setMinOrderQuantity(body.b2bMinOrderQuantity() != null ? body.b2bMinOrderQuantity() : 1); // ← null-check mancante
            }
            priceListsRepository.save(priceList);
        }

        return savedProduct;
    }


    @Transactional
    public Product patchProduct(UUID productId, Map<String, Object> updates) {

        Product found = this.findById(productId);

        updates.forEach((key, value) -> {
            if (value != null) {
                switch (key) {
                    case "productName" -> found.setProductName((String) value);
                    case "productSlug" -> found.setProductSlug((String) value);
                    case "productDescription" -> found.setProductDescription((String) value);
                    case "shortProductDescription" -> found.setShortProductDescription((String) value);
                    case "productIsAvailable" -> found.setProductIsAvailable((boolean) value);
                    case "productStatus" -> found.setProductStatus(ProductStatus.valueOf((String) value));
                    case "productCategoryId" -> {
                        UUID categoryId = UUID.fromString((String) value);
                        found.setProductCategory(productCategoriesService.findById(categoryId));
                    }
                    default -> log.info("Field {} not mapped for PATCH on Product", key);
                }
            }
        });
        Product updated = this.productsRepository.save(found);
        log.info("Product updated successfully, {}", updated);
        return updated;
    }


//    //VALIDATION METADATA
//    private void validateProductAttribute(Product product) {
//        List<ProductCategoryAttribute> schema =
//                productCategoryAttributesRepository.findByProductCategory_ProductCategoryId(product.getProductCategory().getProductCategoryId());
//
//        Map<String, Object> values = product.getTechnicalProdDetails();
//
//        for (ProductCategoryAttribute attr : schema) {
//            if (attr.isRequired() && !values.containsKey(attr.getProdCatAttributeKey())) {
//                throw new IllegalArgumentException("Product category attribute " + attr.getProdCatAttributeKey() + " is required");
//            }
//        }
//    }


    //SOFT DELETE
    @Transactional
    public void softDeleteProduct(UUID productId) {
        Product found = this.findById(productId);

        found.setDeletedAt(LocalDateTime.now());

        found.setProductIsAvailable(false);
        found.setProductStatus(ProductStatus.ARCHIVED);

        this.productsRepository.save(found);
        log.info("Product soft deleted successfully and pun on Archived Status, {}", found);
    }

    //DELETE
    public void deleteProductById(UUID productId) {
        if (!productsRepository.existsById(productId)) throw new NotFoundException("Product not found");
        log.info("Product deleted successfully, productId: {}", productId);
        productsRepository.deleteById(productId);
    }


}
