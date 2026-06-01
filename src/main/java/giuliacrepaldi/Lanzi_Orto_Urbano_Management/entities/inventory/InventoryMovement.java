package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.OrderItem;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.InvMovementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID invMovementId;

    @Column(nullable = false)
    private double quantityInvMovement;
    @Column(nullable = false)
    private BigDecimal priceInvMovement;
    @Column(nullable = false)
    private String reasonInvMovement;
    @Column(nullable = false)
    private LocalDateTime createdAtInvMovement = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvMovementType invMovementType;


    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
}
