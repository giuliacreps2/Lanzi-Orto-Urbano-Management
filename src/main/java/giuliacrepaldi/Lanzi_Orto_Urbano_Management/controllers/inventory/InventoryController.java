package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //SOFT DELETE
    @PostMapping("/{productId}/req-delete")
    @PreAuthorize("hasAuthor('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> getDeleteById(@PathVariable UUID productId) {
        this.inventoryService.getDeleteById(productId);

        log.info("Sent request to delete product with id {}", productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/delete")
    @PreAuthorize("hasAuthor('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteById(@PathVariable UUID productId, @PathVariable AvailabilityStatus availabilityStatus) {
        this.inventoryService.removeProduct(productId, AvailabilityStatus.PENDING_DELETE);

        log.info("Product with id {} has been deleted", productId);
        return ResponseEntity.noContent().build();
    }
}
