package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TokenType;
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
@Table(name = "auth_tokens")
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID authTokenId;

    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private boolean isRevoked = false;
    private String ipAddress;
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
