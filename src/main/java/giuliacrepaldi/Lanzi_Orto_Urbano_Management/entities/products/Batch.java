package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.StatusBatch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID batchId;

    @Column(nullable = false)
    private String batchCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBatch statusBatch;

    @Column(nullable = false)
    private double quantityPlanned;
    @Column(nullable = false)
    private double quantityActual;
    @Column(nullable = false)
    private LocalDateTime startedAt;
    @Column(nullable = false)
    private LocalDate expectedHarvestDate;
    @Column(nullable = false)
    private LocalDate actualHarvestDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> batchMetadata;


    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;
}
