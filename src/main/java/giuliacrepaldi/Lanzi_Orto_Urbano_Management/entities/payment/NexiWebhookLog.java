package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

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
@Table(name = "nexi_webhook_log")
public class NexiWebhookLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID nexiWebhookLogId;

    // Identificativo univoco dell'evento inviato negli header di Nexi (es. Correlation-Id)
    @Column(name = "event_id", unique = true, nullable = false)
    private String eventId;

    @Lob
    @Column(name = "raw_payload", nullable = false)
    private String rawPayload; // Il JSON inviato da Nexi

    private LocalDateTime receivedAt = LocalDateTime.now();

    private boolean processed = false;
}
