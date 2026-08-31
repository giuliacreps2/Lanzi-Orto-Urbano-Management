package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
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
@Table(name = "hosted_order_req_payment")
public class CreatedHostedOrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hostedOrderReqId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    @OneToOne(mappedBy = "createdHostedOrderRequest")
    private NexiPaymentSession nexiPaymentSession;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
