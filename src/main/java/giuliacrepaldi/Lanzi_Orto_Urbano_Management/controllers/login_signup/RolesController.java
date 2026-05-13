package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Role;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.RoleDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.RolesService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }


    //POST
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@RequestBody @Validated RoleDTO body, BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid role");
        return this.rolesService.saveRole(body);
    }

    //GET
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Role findById(@PathVariable UUID roleId) {
        return this.rolesService.findById(roleId);
    }

    @GetMapping("/name/{roleName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Role findByRoleName(@PathVariable String roleName) {
        return this.rolesService.findByRoleName(roleName);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Role> findAll() {
        return this.rolesService.findAll();
    }


    //PUT
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Role findByIdAndUpdate(@PathVariable UUID roleId, @RequestBody @Validated RoleDTO body, BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid role");
        return this.rolesService.findByIdAndUpdate(roleId, body);
    }


    //DELETE
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID roleId) {
        this.rolesService.findByIdAndDelete(roleId);
    }


}
