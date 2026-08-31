package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.payment;

import io.github.nexipayments.sdknpg.securitytokenstorage.ISecurityTokenStorage;
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
@Table(name = "security_token_storage")
public class SecurityTokenStorage implements ISecurityTokenStorage {

    @Column(nullable = false)
    private boolean isRevoked = false;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID securityTokenStorageId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean isUsed;
    private LocalDateTime expiresAt;


    @Override
    public void store(String orderId, String securityToken) {

    }

    @Override
    public boolean verifyExistence(String orderId, String securityToken) {
        // TODO: implementare — verificare esistenza/validità (isUsed, isRevoked, expiresAt)
        return false;
    }
}
