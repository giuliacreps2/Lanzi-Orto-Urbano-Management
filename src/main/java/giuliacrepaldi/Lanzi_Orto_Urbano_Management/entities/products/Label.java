package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "labels")
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID labelId;

    @Column(nullable = false)
    private String barCodeGs1;
    @Column(nullable = false)
    private String barcodeData;
    @Column(nullable = false)
    private LocalDate productionDate;
    @Column(nullable = true)
    private LocalDate bestBeforeDate;
    @Column(nullable = true)
    private LocalDate exitDate;
    @Column(nullable = false)
    private LocalDateTime printedAt;
    @Column(nullable = false)
    private boolean inventoryDecremented;


    @ManyToOne
    @JoinColumn(name = "batch_id")
    @JsonIgnore
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    @JsonIgnore
    private OrderItem orderItem;
}
