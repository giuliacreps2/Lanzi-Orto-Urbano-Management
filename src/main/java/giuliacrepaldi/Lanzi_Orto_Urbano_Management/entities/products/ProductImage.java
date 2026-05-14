package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

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
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID imageProdId;

    @Column(nullable = false)
    private String urlImage;
    private String altText;
    private Integer sortOrder;
    private boolean isPrimary;
    private String cloudinaryPublicId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
