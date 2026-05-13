package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.login_signup;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup.B2cProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2cProfilesService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/b2c")
public class B2cProfilesController {

    private final B2cProfilesService b2cProfilesService;


    public B2cProfilesController(B2cProfilesService b2cProfilesService) {
        this.b2cProfilesService = b2cProfilesService;
    }

    //ADMIN ONLY
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<B2cProfile> getB2bProfile(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "surname") String sortBy) {
        return this.b2cProfilesService.findAll(page, size, sortBy);
    }

    @GetMapping("/{b2cProfileId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public B2cProfile getById(@PathVariable UUID b2cProfileId) {
        return this.b2cProfilesService.findById(b2cProfileId);
    }

    @PutMapping("/{b2cProfileId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public B2cProfile adminUpdate(@PathVariable UUID b2cProfileId,
                                  @RequestBody @Validated
                                  B2cProfileDTO body,
                                  BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        return this.b2cProfilesService.adminUpdateB2cProfile(b2cProfileId, body);
    }

    //METODI SOLO PER B2CPROFILE
    @GetMapping("/me")
    public B2cProfile getOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.b2cProfilesService.findByUserId(currentAuthenticatedUser.getUserId());
    }

    @PutMapping("/me")
    public B2cProfile updateOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser,
                                       @RequestBody @Validated B2cProfileDTO body,
                                       BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        B2cProfile profile = this.b2cProfilesService.findByUserId(currentAuthenticatedUser.getUserId());
        return this.b2cProfilesService.findByIdAndUpdateB2cProfile(profile.getB2cProfileId(), body);
    }

}
