package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.payment.PaymentType;
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
@ToString
@Builder

@Entity
@Table(name = "payments")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentMethodId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> billingDetails;

}
