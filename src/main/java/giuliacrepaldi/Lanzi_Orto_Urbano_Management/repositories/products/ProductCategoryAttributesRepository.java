package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductCategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryAttributesRepository extends JpaRepository<ProductCategoryAttribute, UUID> {

    Optional<ProductCategoryAttribute> findById(UUID prodCatAttributeId);

    List<ProductCategoryAttribute> findByProductId(UUID productId);

    List<ProductCategoryAttribute> findByProductCategory(ProductCategory productCategory);
}
