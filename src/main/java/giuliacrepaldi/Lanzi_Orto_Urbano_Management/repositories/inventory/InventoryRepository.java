package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {


    List<Inventory> findByProductVariant_VariantId(UUID variantId);

    List<Inventory> findByProductVariant_VariantIdAndDeletedAtIsNull(UUID variantId);

    @Modifying
    @Query("UPDATE Inventory i SET i.currentQuantity = i.currentQuantity - :qty " +
            "WHERE i.inventoryId = :inventoryId AND i.currentQuantity >= :qty")
    int decrementQuantity(@Param("inventoryId") UUID inventoryId, @Param("qty") int qty);


    @Modifying
    @Query("UPDATE Inventory i SET i.currentQuantity = i.currentQuantity + :qty " +
            "WHERE i.inventoryId = :inventoryId")
    int incrementQuantity(@Param("inventoryId") UUID inventoryId, @Param("qty") int qty);
}
