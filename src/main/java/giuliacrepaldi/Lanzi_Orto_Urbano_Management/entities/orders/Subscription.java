package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.FrequencySub;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID subscriptionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency_subscriptions", nullable = false)
    private FrequencySub frequencySub;

    @Column(nullable = false)
    private LocalDate nextDeliveryDate;

    @Column(nullable = false)
    private boolean activeSubscription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> dataItemsOrder;

    @ManyToOne
    @JoinColumn(name = "b2c_profile_id")
    private B2cProfile b2cProfile;

    @ManyToOne
    @JoinColumn(name = "b2b_profile_id")
    private B2bProfile b2bProfile;

    @OneToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

}

