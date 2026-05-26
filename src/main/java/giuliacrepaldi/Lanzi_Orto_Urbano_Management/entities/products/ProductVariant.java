package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID variantId;

    @Column(name = "sku_variants", nullable = false, unique = true)
    private String skuVariant;
    @Column(nullable = false)
    private boolean activeVariant;
    @Column
    private Double netWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> technicalDetails;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    private PackagingType packagingType;

    @OneToMany(mappedBy = "productVariant")
    private List<PriceList> priceList;


}
