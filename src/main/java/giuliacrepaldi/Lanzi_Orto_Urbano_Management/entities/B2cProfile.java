package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "b2c_profiles")
public class B2cProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID B2cProfileId;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;

    private String phoneNumber;
    private LocalDate birthDate;
    private String avatar;

    @Column(nullable = false)
    private Long loyaltyPoints = 0L;
    private LocalDateTime loyaltyLastActivity;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    public B2cProfile(String name, String surname, Long loyaltyPoints, User user) {
        this.name = name;
        this.surname = surname;
        this.loyaltyPoints = loyaltyPoints;
        this.user = user;
    }

    public B2cProfile(String name, String surname, String phoneNumber, LocalDate birthDate, String avatar, Long loyaltyPoints, LocalDateTime loyaltyLastActivity, User user) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.avatar = "https://ui-avatars.com/api?name=" + name + "+" + surname;
        this.loyaltyPoints = loyaltyPoints;
        this.loyaltyLastActivity = loyaltyLastActivity;
        this.user = user;
    }
}
