package giuliacrepaldi.Lanzi_Orto_Urbano_Management.config;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getImageUploader(@Value("${cloudinary.name}") String cloudName,
                                       @Value("${cloudinary.apikey}") String apiKey,
                                       @Value("${cloudinary.secret}") String apiSecret) {

        Map<String, String> configuration = new HashMap<>();
        configuration.put("cloud_name", cloudName);
        log.info("Cloudinary configuration loaded first API");
        configuration.put("api_key", apiKey);
        log.info("Cloudinary configuration loaded second API");
        configuration.put("api_secret", apiSecret);
        log.info("Cloudinary configuration loaded third API");

        return new Cloudinary(configuration);
    }
}