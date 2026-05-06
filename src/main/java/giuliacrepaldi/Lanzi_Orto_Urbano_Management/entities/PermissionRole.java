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
public class PermissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permissionRoleId;

    @ManyToOne
    @JoinColumn(name = "admin_role_id")
    private AdminRole adminRole;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public PermissionRole(AdminRole adminRole, Permission permission) {
        this.adminRole = adminRole;
        this.permission = permission;
    }
}
