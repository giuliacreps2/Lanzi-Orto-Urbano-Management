package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permissionId;

    @Column(nullable = false, unique = true)
    private String permissionName;
    @Column(nullable = false)
    private String permissionDescription;


    public Permission(String permissionName, String permissionDescription) {
        this.permissionName = permissionName;
        this.permissionDescription = permissionDescription;
    }
}
