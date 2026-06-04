package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder


@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties("items")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Label> labels = new ArrayList<>();

}
