package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PackagingType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.PackagingTypeDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.PackagingTypesService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/packaging")
public class PackagingTypesController {

    private final PackagingTypesService packagingTypesService;

    public PackagingTypesController(PackagingTypesService packagingTypesService) {
        this.packagingTypesService = packagingTypesService;
    }


    //POST
    @PostMapping("/new-type")
    @PreAuthorize("hasAthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PackagingType createPackagingType(@RequestBody @Validated PackagingTypeDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.packagingTypesService.savePackagingType(body);
    }

    //GET
    @GetMapping("/{packTypeId}")
    public PackagingType findById(UUID packTypeId) {
        return this.packagingTypesService.findById(packTypeId);
    }

    @GetMapping
    public Page<PackagingType> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.packagingTypesService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{packTypeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PackagingType update(@PathVariable UUID packTypeId, @RequestBody @Validated PackagingTypeDTO body) {
        return this.packagingTypesService.findByIdAndUpdate(packTypeId, body);
    }

    //DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(UUID packTypeId) {
        this.packagingTypesService.deleteById(packTypeId);
    }

}
