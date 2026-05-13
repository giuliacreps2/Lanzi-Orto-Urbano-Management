package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

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
    private Integer barcodeData;
    @Column(nullable = false)
    private LocalDate productionDate;
    @Column(nullable = false)
    private LocalDate bestBeforeDate;
    @Column(nullable = false)
    private LocalDate exitDate;
    @Column(nullable = false)
    private LocalDateTime printedAt;
    @Column(nullable = false)
    private boolean inventoryDecremented;


    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;
}
