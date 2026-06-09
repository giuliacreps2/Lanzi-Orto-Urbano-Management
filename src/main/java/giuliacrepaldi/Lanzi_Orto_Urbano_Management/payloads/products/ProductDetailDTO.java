package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

import java.util.List;


public record ProductDetailDTO(
        ProductInfoDTO product,
        List<VariantInfoDTO> variants
) {
}

