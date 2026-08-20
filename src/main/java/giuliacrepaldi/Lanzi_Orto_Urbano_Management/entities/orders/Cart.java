package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    private String emailWithoutAuthUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus cartStatus;

    @Column(nullable = false)
    private LocalDateTime cartCreatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime cartLastActivityAt = LocalDateTime.now();

    private LocalDateTime abandonedEmailScheduledAt;
    private LocalDateTime abandonedEmailSentAt;

    @ManyToOne
    @JoinColumn(name = "b2c_profile_id")
    private B2cProfile b2cProfile;


    @ManyToOne
    @JoinColumn(name = "b2b_profile_id")
    private B2bProfile b2bProfile;

    @OneToOne
    @JoinColumn(name = "converted_order_id", unique = true)
    private Order convertedOrder;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
}
