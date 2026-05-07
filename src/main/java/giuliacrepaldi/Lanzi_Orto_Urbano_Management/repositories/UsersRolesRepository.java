package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRolesRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUser_UserId(UUID userId);

}
