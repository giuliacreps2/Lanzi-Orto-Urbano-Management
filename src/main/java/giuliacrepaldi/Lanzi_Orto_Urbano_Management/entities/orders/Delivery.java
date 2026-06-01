package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusDeliveryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliveryId;

    @Column(nullable = false)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDeliveryType statusDeliveryType;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Column(nullable = false)
    private String recipientName;

    private String driver;

    @Column(nullable = false)
    private BigDecimal priceDelivery;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> shippingAddress;

    @ManyToOne
    @JoinColumn(name = "b2cProfileId")
    private B2cProfile b2cProfile;

    @ManyToOne
    @JoinColumn(name = "b2bProfileId")
    private B2bProfile b2bProfile;

}
