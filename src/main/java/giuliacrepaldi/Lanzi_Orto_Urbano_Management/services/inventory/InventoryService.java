package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    
}
