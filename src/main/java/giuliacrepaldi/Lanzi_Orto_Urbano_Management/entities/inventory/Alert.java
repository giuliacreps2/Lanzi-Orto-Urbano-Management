package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.ChannelAlert;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.StatusAlert;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.TypeAlert;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString


@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID alertId;

    @Column(nullable = false)
    private TypeAlert typeAlert;
    @Column(nullable = false)
    private StatusAlert statusAlert;
    @Column(nullable = false)
    private ChannelAlert channelAlert;
    @Column(nullable = false)
    private LocalDateTime scheduledAlertAt;
    @Column(nullable = false)
    private LocalDateTime sentAlertAt;


    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//    @ManyToOne
//    @JoinColumn(name= "tray_return_id")
//    private TrayReturn trayReturn;
//
//
}
