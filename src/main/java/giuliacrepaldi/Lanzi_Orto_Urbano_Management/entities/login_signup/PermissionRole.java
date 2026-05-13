package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder


@Entity
@Table(name = "permission_roles")
public class PermissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permissionRoleId;

    @ManyToOne
    @JoinColumn(name = "admin_role_id")
    private AdminProfile adminProfile;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public PermissionRole(AdminProfile adminProfile, Permission permission) {
        this.adminProfile = adminProfile;
        this.permission = permission;
    }
}
