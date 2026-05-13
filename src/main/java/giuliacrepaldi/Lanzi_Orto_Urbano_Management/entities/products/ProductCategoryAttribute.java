package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AttributeType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "product_category_attributes")
public class ProductCategoryAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID prodCatAttributeId;

    @Column(nullable = false)
    private String prodCatAttributeKey;

    @Column(nullable = false)
    private String prodCatAttributeLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttributeType attrType;

    private boolean required;
    private String defaultValue;
    private String minValue;
    private String maxValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;


    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;
}
