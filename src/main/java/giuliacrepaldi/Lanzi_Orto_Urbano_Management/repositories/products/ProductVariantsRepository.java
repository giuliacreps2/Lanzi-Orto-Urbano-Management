package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductVariantsRepository extends JpaRepository<ProductVariant, UUID> {
    List<ProductVariant> findByActiveVariantTrue();

    @Query("SELECT v FROM ProductVariant v WHERE v.product.productId = :productId")
    Optional<ProductVariant> findByProductProductId(@Param("productId") UUID productId);
}
