package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.AdminProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.AdminProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.AdminProfilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AdminProfilesService {

    private final AdminProfilesRepository adminProfilesRepository;

    public AdminProfilesService(AdminProfilesRepository adminProfilesRepository) {
        this.adminProfilesRepository = adminProfilesRepository;
    }

    //CREATE ADMIN ROLE
    public AdminProfile saveAdminProfile(User user, AdminProfileDTO body) {

        AdminProfile newAdminProf = AdminProfile.builder()
                .adminProfileName(body.adminProfileName())
                .adminProfileSurname(body.adminProfileSurname())
                .adminProfileLastLoginAt(LocalDateTime.now())
                .user(user)
                .build();

        AdminProfile savedAdminProfile = adminProfilesRepository.save(newAdminProf);

        log.info("Admin Role Saved: {}", savedAdminProfile);

        return savedAdminProfile;
    }

    public void saveAdminProfile(User savedNewUser, String name, String surname) {
    }
}
