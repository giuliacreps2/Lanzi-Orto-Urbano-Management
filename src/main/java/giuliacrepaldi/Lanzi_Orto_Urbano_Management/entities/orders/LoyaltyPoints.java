package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder


@Entity
@Table(name = "loyalty_points")
public class LoyaltyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loyaltyPointsId;

    @Column(nullable = false)
    private LocalDateTime createdAtLoyalPoints = LocalDateTime.now();

    @Column(nullable = false)
    private String descriptionLoyaltyPoints;

    private LocalDateTime updatedAtLoyalPoints;
    private LocalDateTime deletedAtLoyalPoints;


    @ManyToOne
    @JoinColumn(name = "trayReturnId", nullable = false)
    private TrayReturn trayReturn;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;
}
