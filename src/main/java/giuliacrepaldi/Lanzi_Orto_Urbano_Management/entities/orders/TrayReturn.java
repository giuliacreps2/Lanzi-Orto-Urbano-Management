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
@Table(name = "tray_returns")
public class TrayReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trayReturnId;

    @Column(nullable = false)
    private Integer traysReturned;

    @Column(nullable = false)
    private Integer traysAccepted;

    @Column(nullable = false)
    private Integer pointsAwarded;

    @Column(nullable = false)
    private LocalDateTime returnedTrayAt;

    @Column(nullable = false)
    private LocalDateTime validatedTrayAt;

    @Column(nullable = false)
    private boolean validatedByAdmin;

    private String notesTrayRejected;


    @ManyToOne
    @JoinColumn(name = "b2cProfileId", nullable = false)
    private B2cProfile b2cProfile;

    @ManyToOne
    @JoinColumn(name = "b2bProfileId", nullable = false)
    private B2bProfile b2bProfile;


    public Integer getTraysRejected() {
        if (this.traysReturned == null || this.traysAccepted == null) return 0;
        return this.traysReturned - this.traysAccepted;
    }
}
