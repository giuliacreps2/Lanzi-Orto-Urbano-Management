package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PriceList;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductCatalogDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.ProductDTO;
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

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final ProductCategoryAttributesRepository productCategoryAttributesRepository;
    private final ProductCategoriesService productCategoriesService;
    private final ProductVariantsRepository productVariantsRepository;
    private final UsersService usersService;

    public ProductsService(ProductsRepository productsRepository, ProductCategoryAttributesRepository productCategoryAttributesRepository, ProductCategoriesService productCategoriesService, ProductVariantsRepository productVariantsRepository, UsersService usersService) {
        this.productsRepository = productsRepository;
        this.productCategoryAttributesRepository = productCategoryAttributesRepository;
        this.productCategoriesService = productCategoriesService;
        this.productVariantsRepository = productVariantsRepository;
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
        if (
                currentUser != null
                        && currentUser.getB2bProfile() != null
                        && currentUser.getB2bProfile().getStatusB2b() == StatusB2b.APPROVED
        ) {
            return ClientCategory.B2B;
        }
        return ClientCategory.B2C;
    }

    //SWITCH CATALOGO PRODOTTI PER B2C/B2B
    public List<ProductCatalogDTO> getCatalogForUser(User currentUser) {

        ClientCategory clientCategory = resolveClientCategory(currentUser);

        List<ProductVariant> variants = productVariantsRepository.findByActiveVariantTrue();

        return variants.stream()
                .map(variant -> {
                    PriceList priceList = variant.getPriceList()
                            .stream()
                            .filter(price -> price.getClientCategory() == clientCategory)
                            .findFirst()
                            .orElseThrow(() -> new NotFoundException("Price not found for variant " + variant.getVariantId()));

                    Product product = variant.getProduct();

                    String priceLabel = clientCategory == ClientCategory.B2B
                            ? "+IVA"
                            : "IVA inclusa";

                    return new ProductCatalogDTO(
                            product.getProductId(),
                            variant.getVariantId(),
                            product.getProductName(),
                            product.getProductSlug(),
                            product.getShortProductDescription(),
                            variant.getSkuVariant(),
                            variant.getNetWeight(),
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

        if (authentication != null && authentication.isAuthenticated()) {
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

    //DELETE
    public void deleteProductById(UUID productId) {
        if (!productsRepository.existsById(productId)) throw new NotFoundException("Product not found");
        log.info("Product deleted successfully, productId: {}", productId);
        productsRepository.deleteById(productId);
    }
}
