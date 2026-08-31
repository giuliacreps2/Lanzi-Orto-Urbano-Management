package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment.CreatedHostedOrderRequest;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment.NexiPaymentSession;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.ChannelOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.DeliveryType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.SourceOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrder statusOrder;

    @Enumerated(EnumType.STRING)
    @Column
    private ChannelOrder channelOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceOrder sourceOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryType;

    @Column(nullable = false)
    private boolean reorderedFormByAdmin;

    @Column(nullable = false)
    private LocalDateTime orderCreatedAt = LocalDateTime.now();

    private LocalDateTime orderUpdatedAt;

    @Column(nullable = false)
    private boolean loyaltyPointsUsed;

    private BigDecimal loyaltyDiscount;

    @Column(nullable = false)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    private String currency;


    private String orderNotes;

    @Column(nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    private Integer pointsRedeemed; // nullable, null se non ha usato punti

    private String guestEmail;
    private String guestName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "b2c_profile_id")
    private B2cProfile b2cProfile;

    @ManyToOne
    @JoinColumn(name = "b2b_profile_id")
    private B2bProfile b2bProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_session_id", nullable = false)
    private NexiPaymentSession nexiPaymentSession;

    @ManyToMany
    @JoinColumn(name = "hosted_order_req_payment")
    private CreatedHostedOrderRequest createdHostedOrderRequest;

}
