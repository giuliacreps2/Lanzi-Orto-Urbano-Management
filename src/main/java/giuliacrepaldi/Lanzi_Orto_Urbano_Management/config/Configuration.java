package giuliacrepaldi.Lanzi_Orto_Urbano_Management.config;

import io.github.nexipayments.sdknpg.configuration.IConfiguration;
import lombok.Data;
import lombok.ToString;

import java.net.MalformedURLException;
import java.net.URL;

@Data
public class Configuration implements IConfiguration {

    public Configuration(
            final String apiKey = "<2e570a58-9914-477a-9ede-35baff23a376>" // inserire api-key
    ) {
        this.apiKey = apiKey;
    }

    @ToString.Exclude
    private final String apiKey;

    public URL getGatewayBaseUrl() {
        try {
            return new URL("https://stg-ta.nexigroup.com/api/phoenix-0.0/psp/api/v1");  // indirizzo ambiente di test
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

}
