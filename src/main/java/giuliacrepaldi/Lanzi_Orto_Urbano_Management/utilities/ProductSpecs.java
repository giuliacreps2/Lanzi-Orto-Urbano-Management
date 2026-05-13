package giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs {

    public static Specification<Product> hasName(String productName) {
        return (root, query, builder) ->
        {
            if (productName == null ||productName.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("contactName")),
                    "%" + productName.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasPrice(Double price) {
        return (root, query, builder) ->

    }
}
