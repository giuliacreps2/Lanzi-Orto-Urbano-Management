package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;


import com.fasterxml.jackson.annotation.JsonIgnore;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.PackagingCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "packaging_types")
public class PackagingType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID packTypeId;

    @Column(nullable = false)
    private String namePackType;
    @Column(nullable = false)
    private String unitOfMeasure;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> dimensionsJsonb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackagingCategory packagingCategory;


    @JsonIgnore
    @OneToMany(mappedBy = "packagingType", cascade = CascadeType.ALL)
    private List<ProductVariant> productVariants = new ArrayList<>();
}
