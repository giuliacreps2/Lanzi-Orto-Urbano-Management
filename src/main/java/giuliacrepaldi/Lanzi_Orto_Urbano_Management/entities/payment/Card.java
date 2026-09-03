package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.payment.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString


@Entity
@Table(name = "user_cards")
public class Card {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cardId;

    @Column(nullable = false)
    private String nexiContractId;

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false, unique = true)
    private String maskedPan;

    private String tokenCard;

    @Column(nullable = false)
    private String expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
