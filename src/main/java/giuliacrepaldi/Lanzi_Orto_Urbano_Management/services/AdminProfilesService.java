package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.AdminProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.AdminProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.AdminProfilesRepository;
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
