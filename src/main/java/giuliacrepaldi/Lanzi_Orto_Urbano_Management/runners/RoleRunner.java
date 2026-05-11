package giuliacrepaldi.Lanzi_Orto_Urbano_Management.runners;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Role;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RoleDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.RolesRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.UsersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.RolesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.UsersRolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class RoleRunner implements CommandLineRunner {

    private final RolesService rolesService;
    private final UsersRepository usersRepository;
    private final UsersRolesService usersRolesService;
    private final PasswordEncoder bcrypt;
    private final RolesRepository rolesRepository;

    @Value("${admin.default.email}")
    private String adminEmail;
    @Value("${admin.default.password}")
    private String adminPassword;
    @Value("${admin.default.name}")
    private String adminName;
    @Value("${admin.default.surname}")
    private String adminSurname;

    public RoleRunner(RolesService rolesService, UsersRepository usersRepository, UsersRolesService usersRolesService, PasswordEncoder bcrypt, RolesRepository rolesRepository) {
        this.rolesService = rolesService;
        this.usersRepository = usersRepository;
        this.usersRolesService = usersRolesService;
        this.bcrypt = bcrypt;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> rolesToCreate = List.of("ADMIN", "USER");

        if (!rolesService.existsByRoleName("ADMIN")) {
            rolesService.saveRole(new RoleDTO("ADMIN"));
            log.info("Admin has been created");
        }

        for (String roleName : rolesToCreate) {
            if (!rolesRepository.existsByRoleName(roleName)) {
                rolesRepository.save(new Role(roleName));
            }
        }

        if (!usersRepository.existsByEmail(adminEmail)) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(bcrypt.encode(adminPassword));
            adminUser.setActive(true);
            adminUser.setEmailVerified(true);
            adminUser.setEnabled(true);
            adminUser.setPrivacyAccepted(true);
            adminUser.setPrivacyAcceptedAt(LocalDateTime.now());
            adminUser.setCreatedAt(LocalDateTime.now());
            adminUser.setUpdatedAt(LocalDateTime.now());
            adminUser.setDeletedAt(null);

            User savedAdmin = this.usersRepository.save(adminUser);

//            adminProfilesService.saveAdminProfile(savedAdmin, adminName);

            Role adminRole = rolesService.findByRoleName("ADMIN");
            usersRolesService.saveUserRole(savedAdmin, adminRole);

            log.info("Default admin created: {}", adminEmail);
        } else {
            log.info("Admin already exists, skipping creation");
        }
    }
}

