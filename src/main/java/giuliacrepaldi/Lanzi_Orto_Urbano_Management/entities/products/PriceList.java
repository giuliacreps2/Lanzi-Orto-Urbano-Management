package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "price_lists")
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID priceListId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientCategory clientCategory;

    @Column(nullable = false)
    private double price;

    private Integer minOrderQuantity;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

}
