package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PriceList;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.PriceListDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.PriceListsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/price-list")
public class PriceListsController {

    private final PriceListsService priceListsService;

    public PriceListsController(PriceListsService priceListsService) {
        this.priceListsService = priceListsService;
    }

    //POST
    @PostMapping("/new-prz")
    @PreAuthorize("hasAthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PriceList createPriceList(@RequestBody @Validated PriceListDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.priceListsService.saveNewPriceList(body);
    }

    //GET
    @GetMapping("/{priceListId}")
    public PriceList findById(UUID priceListId) {
        return this.priceListsService.findById(priceListId);
    }


    @GetMapping
    public Page<PriceList> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.priceListsService.findAll(page, size, sortBy);
    }

    //UPDATE
    @PutMapping("/{priceListId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PriceList update(@RequestBody @Validated PriceListDTO body, BindingResult validation) {
        return this.priceListsService.saveNewPriceList(body);
    }


    //DELETE
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(UUID priceListId) {
        this.priceListsService.deletePriceListById(priceListId);
    }
}
