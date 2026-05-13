package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductImagesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductImagesServices {

    private final ProductImagesRepository productImagesRepository;
    private final Cloudinary cloudinaryUploader;

    public ProductImagesServices(ProductImagesRepository productImagesRepository, Cloudinary cloudinaryUploader) {
        this.productImagesRepository = productImagesRepository;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    //CREATE
    public void imageUpload(MultipartFile file, UUID productImageId) {
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            System.out.println(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
