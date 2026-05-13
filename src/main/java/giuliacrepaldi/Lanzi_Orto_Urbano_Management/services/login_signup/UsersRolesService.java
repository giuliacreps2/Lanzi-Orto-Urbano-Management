package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Role;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.UserRole;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.UsersRolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsersRolesService {

    private final UsersRolesRepository usersRolesRepository;

    public UsersRolesService(UsersRolesRepository usersRolesRepository) {
        this.usersRolesRepository = usersRolesRepository;
    }

    //CREATE
    public UserRole saveUserRole(User user, Role role) {
        UserRole userRole = new UserRole(user, role);
        log.info("Saving user role for userId={} roleId={}", user.getUserId(), role.getRoleId());
        return this.usersRolesRepository.save(userRole);
    }

    //REQUESTS
    public UserRole findById(UUID id) {
        return this.usersRolesRepository.findById(id).orElseThrow(() -> new NotFoundException("User role not found"));
    }


    public List<UserRole> findByUserId(UUID userId) {
        return this.usersRolesRepository.findByUser_UserId(userId);
    }


    public Page<UserRole> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRolesRepository.findAll(pageable);
    }

    //DELETE
    public void deleteByUserId(UUID userId) {
        UserRole found = this.usersRolesRepository.findById(userId).orElseThrow(() -> new NotFoundException("User role not found"));
        log.info("Delete user roles by user id {}", found.getUser().getUserId());
        this.usersRolesRepository.delete(found);
    }

    public void findByIdAndDelete(UUID userRoleId) {
        UserRole found = this.findById(userRoleId);
        log.info("Delete user roles by id {}", found);
        this.usersRolesRepository.delete(found);
    }
}
