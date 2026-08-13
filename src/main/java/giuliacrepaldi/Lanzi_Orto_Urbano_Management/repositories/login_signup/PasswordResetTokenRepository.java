package giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.PasswordResetToken;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    PasswordResetToken findByResetToken(String resetToken);

    List<PasswordResetToken> findByUserAndUsedFalse(User user);
}