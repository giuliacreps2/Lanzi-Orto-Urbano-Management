package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "'packaging_types'")
public class PackagingType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID packTypeId;

    private String namePackType;
    private String unitOfMeasure;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> dimensionsJsonb;

    @Enumerated
    @Column
    private PackagingType packagingType;
}
