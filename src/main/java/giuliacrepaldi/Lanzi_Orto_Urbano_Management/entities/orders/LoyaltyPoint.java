package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
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
public class LoyaltyPoint {

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
    @JoinColumn(name = "trayReturnId")
    private TrayReturn trayReturn;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "b2c_profile_id")
    private B2cProfile b2cProfile;

    @ManyToOne
    @JoinColumn(name = "b2b_profile_id")
    private B2bProfile b2bProfile;
}
