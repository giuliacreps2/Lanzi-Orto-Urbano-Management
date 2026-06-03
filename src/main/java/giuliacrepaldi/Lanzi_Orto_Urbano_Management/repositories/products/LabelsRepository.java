package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabelsRepository extends JpaRepository<Label, UUID> {

    List<Label> findByOrderItem_Order_OrderId(UUID orderId);

    List<Label> findByOrderItem_OrderItemId(UUID orderItemId);

    boolean existsByOrderItem_OrderItemId(UUID orderItemId);
}
