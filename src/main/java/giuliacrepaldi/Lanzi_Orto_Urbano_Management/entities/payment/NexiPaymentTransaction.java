package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.payment.PaymentStatus;
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
@Table(name = "payment_transaction")
public class NexiPaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID nexiPaymentTransactionId;

    // Identificativi nativi inviati dal gateway Nexi nei webhook/callback
    @Column(name = "nexi_operation_id")
    private String nexiOperationId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String currency;

    private String paymentCircuit;
    private String paymentInstrumentInfo;

    private String errorCode;
    private String errorDescription;


    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private NexiPaymentSession nextPaymentSession;
}
