package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductImage;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductImagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ProductImagesServices {

    private final ProductImagesRepository productImagesRepository;
    private final ProductsService productsService;
    private final Cloudinary cloudinaryUploader;

    public ProductImagesServices(ProductImagesRepository productImagesRepository, ProductsService productsService, Cloudinary cloudinaryUploader) {
        this.productImagesRepository = productImagesRepository;
        this.productsService = productsService;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    //CREATE
    public ProductImage saveImage(MultipartFile file, UUID productId,
                                  String altText, Integer sortOrder, boolean isPrimary) {
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            log.info(url);

            Product product = productsService.findById(productId);

            ProductImage image = ProductImage.builder()
                    .urlImage(url)
                    .altText(altText)
                    .sortOrder(sortOrder)
                    .isPrimary(isPrimary)
                    .product(product)
                    .build();

            return productImagesRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    //FINDBY
    public ProductImage findById(UUID imageProdId) {
        return productImagesRepository.findById(imageProdId).orElseThrow(() -> new NotFoundException("Image not found"));
    }

    public Page<ProductImage> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.productImagesRepository.findAll(pageable);
    }

    //UPDATE
    public ProductImage setPrimary(UUID imageProdId, boolean isPrimary) {

        ProductImage found = this.productImagesRepository.findById(imageProdId).orElseThrow(() -> new NotFoundException("Image not found"));
        found.setPrimary(isPrimary);
        ProductImage savedProdImage = this.productImagesRepository.save(found);

        log.info("Product Image with id: " + imageProdId + " has been updated primary Image successfully");
        return savedProdImage;
    }

    public ProductImage updateAlteTextAndOrder(UUID imageProdId, String altText, Integer sortOrder) {

        ProductImage found = this.productImagesRepository.findById(imageProdId).orElseThrow(() -> new NotFoundException("Image not found"));
        found.setAltText(altText);
        found.setSortOrder(sortOrder);
        ProductImage savedProdImage = this.productImagesRepository.save(found);

        log.info("Product Image with id: " + imageProdId + " has been updated successfully");
        return savedProdImage;
    }


    //DELETE
    public void delete(UUID imageProdId) {
        try {
            String publicId;


            Map result = cloudinaryUploader.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            log.info(url);

            log.info("deleted product image successfully");
            productImagesRepository.deleteById(imageProdId);

        } catch (IOException e) {
            throw new RuntimeException("Cloudinary delete failed", e);
        }
    }


}



