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
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(nullable = false)
    private String roleName;


    public Role(String roleName) {
        this.roleName = roleName;
    }
}
