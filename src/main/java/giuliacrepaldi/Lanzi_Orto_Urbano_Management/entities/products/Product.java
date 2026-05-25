package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(nullable = false)
    private String productSlug;
    @Column(name = "product_description", nullable = false)
    private String productDescription;
    private String shortProductDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus;

    @Column(nullable = false)
    private boolean productIsAvailable;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus productStatus = ProductStatus.DRAFT;


    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
