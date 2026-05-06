package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID registrationRequestId;

    private String email;
    private String verificationToken;
    private LocalDateTime tokenExpiresAt;
    private boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
}
