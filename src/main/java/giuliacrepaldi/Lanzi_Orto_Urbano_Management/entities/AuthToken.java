package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID authTokenId;

    @Column(nullable = false, unique = true)
    private String token;


}
