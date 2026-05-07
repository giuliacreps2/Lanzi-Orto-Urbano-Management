package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.Role;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.RoleDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    //CREATE
    //TODO:CONTROLLARE SE ROLE O ROLEDTO
    public Role saveRole(RoleDTO body) {
        //TODO: CONTROLLI DI CHE TIPO?

        Role newRole = new Role(body.roleName());
        this.rolesRepository.save(newRole);

        log.info("Role saved with roleName={}", newRole.getRoleName());
        return newRole;
    }


    //REQUESTS
    public Role findById(UUID roleId) {
        return this.rolesRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
    }

    public Role findByRoleName(String roleName) {
        return this.rolesRepository.findByRoleName(roleName).orElseThrow(() -> new NotFoundException("Role name not found"));
    }

    public List<Role> findAll() {
        List<Role> roles = this.rolesRepository.findAll();
        return roles;
    }


    //UPDATE
    public Role findByIdAndUpdate(UUID roleId, RoleDTO body) {
        Role found = this.rolesRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
        found.setRoleName(body.roleName());
        this.rolesRepository.save(found);

        log.info("{} is updated successfully", found.getRoleName());
        return found;
    }

    //DELETE
    public void findByIdAndDelete(UUID roleId) {
        Role found = this.rolesRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found"));
        this.rolesRepository.delete(found);
    }

}
