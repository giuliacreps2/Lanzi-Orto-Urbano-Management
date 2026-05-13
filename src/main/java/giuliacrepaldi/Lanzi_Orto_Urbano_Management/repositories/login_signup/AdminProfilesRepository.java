package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminProfilesRepository extends JpaRepository<AdminProfile, UUID> {
}
