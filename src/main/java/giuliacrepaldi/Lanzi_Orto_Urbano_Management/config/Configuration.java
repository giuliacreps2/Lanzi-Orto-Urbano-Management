package giuliacrepaldi.Lanzi_Orto_Urbano_Management.config;

import io.github.nexipayments.sdknpg.configuration.IConfiguration;
import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class Configuration implements IConfiguration {

    private final String apiKey;

    public Configuration(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public URL getGatewayBaseUrl() {
        try {
            return new URL("https://stg-ta.nexigroup.com/api/phoenix-0.0/psp/api/v1");
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}


