package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceListsRepository extends JpaRepository<PriceList, UUID> {
    @Query("SELECT p FROM PriceList p WHERE p.productVariant.variantId = :variantId")
    List<PriceList> findByProductVariantId(@Param("variantId") UUID variantId);
}
