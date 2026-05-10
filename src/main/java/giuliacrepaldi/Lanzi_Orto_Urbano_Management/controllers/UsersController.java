package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.UserDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    //METODI PER ADMIN
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "email") String sortBy) {
        return this.usersService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User getById(@PathVariable UUID userId) {
        return this.usersService.findById(userId);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User getByEmail(@PathVariable String email) {
        return this.usersService.findByEmail(email);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User updateUser(@PathVariable UUID userId,
                           @RequestBody @Validated
                           UserDTO body,
                           BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        return this.usersService.findByIdAndUpdate(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        log.info("Delete user with id: " + userId);
        this.usersService.findByIdAndUpdate(userId, null);
    }


    //METODI SOLO PER USER
    @GetMapping("/me")
    public User getOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser,
                                 @RequestBody @Validated UserDTO body,
                                 BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getUserId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getUserId(), null);
    }
}
