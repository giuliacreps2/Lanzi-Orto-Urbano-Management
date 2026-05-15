package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;
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
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID inventoryId;

    @Column(nullable = false)
    private String inventoryName;
    @Column(nullable = false)
    private Integer currentQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private Integer minThreshold;

    @Column(nullable = false)
    private LocalDateTime deletedAt;


    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

}
