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
@Builder


@Entity
@Table(name = "admin_roles")
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminRoleId;

    @Column(nullable = false)
    private String adminRoleName;
    @Column(nullable = false)
    private String adminRoleSurname;
    @Column(nullable = false)
    private LocalDateTime adminRoleLastLoginAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


//    public AdminRole(String adminRoleName, String adminRoleSurname, LocalDateTime adminRoleLastLoginAt, User user) {
//        this.adminRoleName = adminRoleName;
//        this.adminRoleSurname = adminRoleSurname;
//        this.adminRoleLastLoginAt = adminRoleLastLoginAt;
//        this.user = user;
//    }
}
