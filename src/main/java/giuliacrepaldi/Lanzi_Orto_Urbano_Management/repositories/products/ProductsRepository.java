package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductIdAndAvailabilityStatus(UUID productId, AvailabilityStatus availabilityStatus);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productCategory WHERE p.productSlug = :slug AND p.deletedAt IS NULL")
    Optional<Product> findByProductSlug(@Param("slug") String slug);
//    ClientCategory findClientCategoryByUser(User user);
}
