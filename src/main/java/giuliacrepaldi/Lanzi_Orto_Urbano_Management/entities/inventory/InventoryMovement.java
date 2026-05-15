package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.InvMovementType;
import jakarta.persistence.*;
import lombok.*;

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
    private double priceInvMovement;
    @Column(nullable = false)
    private String reasonInvMovement;
    @Column(nullable = false)
    private LocalDateTime createdAtInvMovement = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvMovementType invMovementType;

    @Column(nullable = false)
    private UUID refId;


    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;
}
