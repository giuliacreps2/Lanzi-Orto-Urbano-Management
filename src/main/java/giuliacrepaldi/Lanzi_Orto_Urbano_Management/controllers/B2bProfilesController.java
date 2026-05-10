package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.B2bProfileDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.B2bProfilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/b2b")
public class B2bProfilesController {

    private final B2bProfilesService b2bProfilesService;

    public B2bProfilesController(B2bProfilesService b2bProfilesService) {
        this.b2bProfilesService = b2bProfilesService;
    }

    //ADMIN ONLY
    @GetMapping("/details")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<B2bProfile> searchDetails(@RequestParam(required = false) String contactName,
                                          @RequestParam(required = false) String contactSurname,
                                          @RequestParam(required = false) String vatNumber,
                                          @RequestParam(required = false) String fiscalCode,
                                          @RequestParam(required = false) String contactPhone,
                                          @RequestParam(required = false) String provinceName,
                                          @RequestParam(required = false) String companyName,
                                          @RequestParam(required = false) String contactEmail,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "companyName") String sortBy,
                                          @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.b2bProfilesService.search(contactName, contactSurname, vatNumber, fiscalCode, contactPhone, provinceName, companyName, contactEmail, pageable);
    }

    @GetMapping("/{b2cProfileId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public B2bProfile getById(@PathVariable UUID b2bProfileId) {
        return this.b2bProfilesService.findById(b2bProfileId);
    }

    @PutMapping("/{b2cProfileId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public B2bProfile adminUpdate(@PathVariable UUID b2bProfileId,
                                  @RequestBody @Validated
                                  B2bProfileDTO body,
                                  BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        return this.b2bProfilesService.findByIdAndUpdateB2bProfile(b2bProfileId, body);
    }

    //METODI SOLO PER B2BPROFILE
    @GetMapping("/me")
    public B2bProfile getOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.b2bProfilesService.findByUserId(currentAuthenticatedUser.getUserId());
    }

    @PutMapping("/me")
    public B2bProfile updateOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser,
                                       @RequestBody @Validated B2bProfileDTO body,
                                       BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException("Invalid user data");
        B2bProfile profile = this.b2bProfilesService.findByUserId(currentAuthenticatedUser.getUserId());
        return this.b2bProfilesService.findByIdAndUpdateB2bProfile(profile.getB2bProfileId(), body);
    }


}
