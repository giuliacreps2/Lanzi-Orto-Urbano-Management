package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepository extends JpaRepository<Role, UUID> {
    boolean existsByRoleName(String roleName);

    Optional<Role> findById(UUID roleId);

    Optional<Role> findByRoleName(String roleName);


}
