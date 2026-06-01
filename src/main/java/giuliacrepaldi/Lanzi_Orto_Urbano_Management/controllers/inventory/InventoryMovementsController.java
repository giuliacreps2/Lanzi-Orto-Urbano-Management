package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.InventoryMovement;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory.InventoryMovementDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory.InventoryMovementsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory-movements")
public class InventoryMovementsController {

    private final InventoryMovementsService inventoryMovementsService;

    public InventoryMovementsController(InventoryMovementsService inventoryMovementsService) {
        this.inventoryMovementsService = inventoryMovementsService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryMovement createInvMovement(@RequestBody @Validated InventoryMovementDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage).toList();
            throw new ValidationException(errors);
        }
        return this.inventoryMovementsService.saveNewInventoryMovement(body);
    }
}
