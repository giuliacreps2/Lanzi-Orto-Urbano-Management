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

}
