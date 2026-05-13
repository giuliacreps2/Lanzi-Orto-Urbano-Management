package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup;

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
@Table(name = "admin_roles")
public class AdminProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminProfileId;

    @Column(nullable = false)
    private String adminProfileName;
    @Column(nullable = false)
    private String adminProfileSurname;
    @Column(nullable = false)
    private LocalDateTime adminProfileLastLoginAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


}
