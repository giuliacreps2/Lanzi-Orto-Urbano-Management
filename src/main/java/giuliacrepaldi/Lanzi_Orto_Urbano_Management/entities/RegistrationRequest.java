package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "registration_requests")
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID registrationRequestId;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String verificationToken;
    @Column(nullable = false)
    private LocalDateTime tokenExpiresAt;
    @Column(nullable = false)
    private boolean isUsed;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientCategory clientCategory;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> metadata;

//    public RegistrationRequest(String email, String verificationToken, LocalDateTime tokenExpiresAt, boolean isUsed, LocalDateTime createdAt, LocalDateTime usedAt, RequestedRole requestedRole, Map<String, Object> metadata) {
//        this.email = email;
//        this.verificationToken = verificationToken;
//        this.tokenExpiresAt = LocalDateTime.now().plusDays(1);
//        this.isUsed = isUsed;
//        this.createdAt = LocalDateTime.now();
//        this.usedAt = usedAt;
//        this.requestedRole = requestedRole;
//        this.metadata = metadata;
//    }
//
//    public RegistrationRequest(String verificationToken, LocalDateTime tokenExpiresAt, boolean isUsed, LocalDateTime createdAt, LocalDateTime usedAt, RequestedRole requestedRole, Map<String, Object> metadata) {
//        this.verificationToken = verificationToken;
//        this.tokenExpiresAt = LocalDateTime.now().plusDays(1);
//        this.isUsed = isUsed;
//        this.createdAt = LocalDateTime.now();
//        this.usedAt = usedAt;
//        this.requestedRole = requestedRole;
//        this.metadata = metadata;
//    }

}
