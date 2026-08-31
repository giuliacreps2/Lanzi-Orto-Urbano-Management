package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.payment.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder


@Entity
@Table(name = "payment_session")
public class NexiPaymentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID nexiPaymentSessionId;

    @Column(nullable = false)
    private String securityTokenSession;

    @Column(name = "hosted_page_url", length = 1024, nullable = false)
    private String hostedPageUrl;

    @Column(nullable = false, length = 2048)
    private String resultUrl;

    @Column(length = 2048)
    private String cancelUrl;

    @Column(length = 2048)
    private String notificationUrl;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus = SessionStatus.CREATED;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;


    @OneToOne
    @JoinColumn(name = "hosted_order_req_payment")
    private CreatedHostedOrderRequest createdHostedOrderRequest;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
