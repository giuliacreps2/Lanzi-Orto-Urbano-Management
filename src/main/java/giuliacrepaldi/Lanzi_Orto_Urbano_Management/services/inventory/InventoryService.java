package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.Inventory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.inventory.InventoryMovement;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Label;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Product;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.inventory.InvMovementType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.products.AvailabilityStatus;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.InsufficientStockException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory.InventoryDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory.StockAvailabilityResponse;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory.InventoryMovementsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.inventory.InventoryRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductVariantsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductVariantsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductsRepository productsRepository;
    private final ProductVariantsService productVariantsService;
    private final ProductVariantsRepository productVariantsRepository;
    private final InventoryMovementsRepository inventoryMovementsRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductsRepository productsRepository, ProductVariantsService productVariantsService, ProductVariantsRepository productVariantsRepository, InventoryMovementsRepository inventoryMovementsRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productsRepository = productsRepository;
        this.productVariantsService = productVariantsService;
        this.productVariantsRepository = productVariantsRepository;
        this.inventoryMovementsRepository = inventoryMovementsRepository;
    }

    //CREATE
    public Inventory saveNewInventory(InventoryDTO body) {
        ProductVariant variant = this.productVariantsService.findById(body.variant());


        Inventory inventory = Inventory.builder()
                .inventoryName(body.inventoryName())
                .currentQuantity(body.currentQuantity())
                .unit(body.unit())
                .minThreshold(body.minThreshold())
                .productVariant(variant)
                .build();

        Inventory savedInventory = this.inventoryRepository.save(inventory);
        log.info("Inventory saved successfully");
        return savedInventory;
    }

    //REQUESTS
    public Inventory findById(UUID inventoryId) {
        return this.inventoryRepository.findById(inventoryId).orElseThrow(() -> new NotFoundException("Inventory not found"));
    }

    public Page<Inventory> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.inventoryRepository.findAll(pageable);
    }

    //UPDATE
    public Inventory findByIdAndUpdate(UUID inventoryId, InventoryDTO body) {

        Inventory found = this.inventoryRepository.findById(inventoryId).orElseThrow(() -> new NotFoundException("Inventory not found"));

        found.setInventoryName(body.inventoryName());
        found.setCurrentQuantity(body.currentQuantity());
        found.setUnit(body.unit());
        found.setMinThreshold(body.minThreshold());

        Inventory savedInventory = this.inventoryRepository.save(found);

        log.info("Inventory updated successfully: {}", savedInventory);

        return savedInventory;
    }

    //DELETE
    public void getDeleteById(UUID productId) {

        Product found = this.productsRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        found.setAvailabilityStatus(AvailabilityStatus.PENDING_DELETE);
        this.productsRepository.save(found);
    }

    @Transactional
    public void removeProduct(UUID productId, AvailabilityStatus availabilityStatus) {
        Product found = this.productsRepository.findByProductIdAndAvailabilityStatus(productId, AvailabilityStatus.PENDING_DELETE)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        found.setDeletedAt(LocalDateTime.now());
        this.productsRepository.save(found);
    }


    //DECREMENTO MAGAZZINO
    public void processInventoryDecrement(Label label, Map<String, Object> config) {

        //Recupero il JSNOB dalla categoria
        Map<String, Object> metadata = (Map<String, Object>) config.get("inventory_logic");
        String decrementType = (String) metadata.get("decrement_type");
        List<String> components = (List<String>) metadata.get("components");

        if ("COMPONENTS".equals(decrementType)) {
            //Per i funghi/microgreens ad es. attuo una strategia di estrazione quantità
            // e decremento nel magazzino
            for (String componentKey : components) {
                Double amount = extractQuantityFromBatch(label.getBatch(), componentKey);
                this.executeDecrement(componentKey, amount, String.valueOf(label.getBatch().getBatchId()));
            }
        }
    }

    private Double extractQuantityFromBatch(Batch batch, String componentKey) {

        String jsonKey = componentKey + "_quantity_g";
        Object value = batch.getBatchMetadata().get(jsonKey);
        return value != null ? Double.valueOf(value.toString()) : 0.0;
    }

    public void executeDecrement(String componentKey, Double amount, String batchId) {

    }

    public void updateQuantity(UUID inventoryId, double quantity) {
        Inventory found = this.inventoryRepository.findById(inventoryId).orElseThrow(() -> new NotFoundException("Inventory not found"));

        found.setCurrentQuantity(found.getCurrentQuantity() + (int) quantity);
        this.inventoryRepository.save(found);
    }


    //CONOSCERE LA QUANTITà DELLE VARIANTI
    public StockAvailabilityResponse getAvailableQuantity(UUID variantId) {
        List<Inventory> found = this.inventoryRepository.findByProductVariant_VariantIdAndDeletedAtIsNull(variantId);

        if (found.isEmpty()) {
            return new StockAvailabilityResponse(false, null);
        } else {
            int quantity = found.stream().mapToInt(Inventory::getCurrentQuantity).sum();
            return new StockAvailabilityResponse(true, quantity);
        }
    }


    //CONFRONTA TRA QUANTITà DISPONIBILE E QUANTITà RICHIESTA
    //RISERVANDO LA QUANTITà RICHIESTA, GESTENDO EVENTUALI RICHIESTE CONCORRENTI

    public void reserveStock(UUID variantId, int quantity) {
        List<Inventory> found = this.inventoryRepository.findByProductVariant_VariantIdAndDeletedAtIsNull(variantId);

        if (found.isEmpty()) {
            return;
        }

        Inventory inventory = found.get(0);

        int rowsUpdated = this.inventoryRepository.decrementQuantity(inventory.getInventoryId(), quantity);

        if (rowsUpdated == 0) {
            throw new InsufficientStockException("Variant not available");
        }
        InventoryMovement newInvMov = InventoryMovement.builder()
                .quantityInvMovement(quantity)
                .invMovementType(InvMovementType.OUT)
                .reasonInvMovement("Order reservation")
                .inventory(inventory)
                .build();

        this.inventoryMovementsRepository.save(newInvMov);
    }


    //RIMETTE A MAGAZZINO I PEZZI DI UN ORDINE NON ANDATO A BUON FINE
    public void releaseStock(UUID variantId, int quantity) {
        List<Inventory> found = this.inventoryRepository.findByProductVariant_VariantIdAndDeletedAtIsNull(variantId);

        if (found.isEmpty()) {
            return;
        }

        Inventory inventory = found.get(0);

        int rowsUpdated = this.inventoryRepository.incrementQuantity(inventory.getInventoryId(), quantity);

        if (rowsUpdated == 0) {
            throw new NotFoundException("Variant not found");
        }

        InventoryMovement newInvMov = InventoryMovement.builder()
                .quantityInvMovement(quantity)
                .invMovementType(InvMovementType.IN)
                .reasonInvMovement("Order cancelled")
                .inventory(inventory)
                .build();

        this.inventoryMovementsRepository.save(newInvMov);
    }


}
