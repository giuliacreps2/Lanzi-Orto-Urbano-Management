package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.Inventory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.InventoryMovement;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory.InventoryMovementDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory.InventoryMovementsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class InventoryMovementsService {

    private final InventoryMovementsRepository inventoryMovementsRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    public InventoryMovementsService(InventoryMovementsRepository inventoryMovementsRepository, InventoryRepository inventoryRepository, InventoryService inventoryService) {
        this.inventoryMovementsRepository = inventoryMovementsRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
    }

    public InventoryMovement saveNewInventoryMovement(InventoryMovementDTO body) {

        //trova inventory per inventoryId, altrimenti NotFoundException
        Inventory foundInventory = this.inventoryRepository.findById(body.inventoryId()).orElseThrow(() -> new NotFoundException("Inventory Not Found"));

        //SE quantity <= 0
        //    lancia BadRequestException "Quantità movimento non valida"
        if (body.quantityInvMovement() <= 0) {
            throw new BadRequestException("Inventory has quantity less than 0");
        }

        //SE invMovementType è IN (carico)
        //    chiama inventoryService.updateQuantity(inventoryId, +quantity)

        //SE invMovementType è OUT (scarico, SALE, LABEL_PRINT)
        //    chiama inventoryService.updateQuantity(inventoryId, -quantity)

        //SE invMovementType è ADJUSTMENT
        //    // delta può essere positivo o negativo, passalo direttamente
        //    chiama inventoryService.updateQuantity(inventoryId, quantity come delta)

        switch (body.invMovementType()) {
            case IN -> inventoryService.updateQuantity(foundInventory.getInventoryId(), body.quantityInvMovement());
            case OUT, SALE, LABEL_PRINT ->
                    inventoryService.updateQuantity(foundInventory.getInventoryId(), -body.quantityInvMovement());
            case ADJUSTMENT ->
                    inventoryService.updateQuantity(foundInventory.getInventoryId(), body.quantityInvMovement());
        }


        //crea InventoryMovement con:
        //    - inventory = inventory trovato
        //    - quantityInvMovement = quantity
        //    - priceInvMovement = price
        //    - reasonInvMovement = reason
        //    - invMovementType = tipo passato
        //    - refId = refId passato
        //    - createdAtInvMovement = now()
        //
        //salva e ritorna

        InventoryMovement inventoryMovement = InventoryMovement.builder()
                .inventory(foundInventory)
                .quantityInvMovement(body.quantityInvMovement())
                .priceInvMovement(body.priceInvMovement())
                .reasonInvMovement(body.reasonInvMovement())
                .createdAtInvMovement(LocalDateTime.now())
                .build();

        return this.inventoryMovementsRepository.save(inventoryMovement);

    }

}
