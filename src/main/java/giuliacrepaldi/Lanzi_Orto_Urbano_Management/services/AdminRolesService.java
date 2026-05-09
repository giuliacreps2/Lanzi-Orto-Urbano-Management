package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.AdminRole;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.AdminRoleDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.AdminRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminRolesService {

    private final AdminRolesRepository adminRolesRepository;
    private final PasswordEncoder bcrypt;

    public AdminRolesService(AdminRolesRepository adminRolesRepository, PasswordEncoder bcrypt) {
        this.adminRolesRepository = adminRolesRepository;
        this.bcrypt = bcrypt;
    }

    //CREATE ADMIN ROLE
    public AdminRole saveAdminRole(AdminRoleDTO body) {

        if (this.adminRolesRepository.existsByEmail(body.email()))
            throw new BadRequestException("Email" + body.email() + "already Exists");

        AdminRole newAdminRole = AdminRole.builder()
                .adminRoleName(body.adminRoleName())
                .adminRoleName(body.adminRoleSurname())
                .build();

        AdminRole savedAdminRole = adminRolesRepository.save(newAdminRole);

        log.info("Admin Role Saved: {}", savedAdminRole);

        return savedAdminRole;
    }
}
