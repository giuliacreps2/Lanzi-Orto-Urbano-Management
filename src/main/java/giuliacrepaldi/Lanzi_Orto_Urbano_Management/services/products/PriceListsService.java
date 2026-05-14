package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.PriceList;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products.PriceListDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.PriceListsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PriceListsService {

    private final PriceListsRepository priceListsRepository;
    private final ProductVariantsService productVariantsService;

    public PriceListsService(PriceListsRepository priceListsRepository, ProductVariantsService productVariantsService) {
        this.priceListsRepository = priceListsRepository;
        this.productVariantsService = productVariantsService;
    }

    //CREATE
    public PriceList saveNewPriceList(PriceListDTO body) {

        PriceList newPriceList = PriceList.builder()
                .price(body.price())
                .minOrderQuantity(body.minOrderQuantity())
                .build();

        PriceList savedPriceList = priceListsRepository.save(newPriceList);
        log.info("Price List saved successfully, {}", savedPriceList);

        return savedPriceList;
    }

    //REQUESTS
    public PriceList findById(UUID priceListId) {
        return this.priceListsRepository.findById(priceListId).orElseThrow(() -> new NotFoundException("Price List not found"));
    }

    public Page<PriceList> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.priceListsRepository.findAll(pageable);
    }

    //UPDATE

    public PriceList findByIdAndUpdatePriceList(UUID priceListId, PriceListDTO body) {
        if (!priceListsRepository.existsById(priceListId)) throw new NotFoundException("Price List not found");

        PriceList found = this.findById(priceListId);

        found.setPrice(body.price());
        found.setMinOrderQuantity(body.minOrderQuantity());
        found.setClientCategory(body.clientCategory());
        found.setProductVariant(productVariantsService.findById(body.productVariantId()));

        PriceList updated = this.priceListsRepository.save(found);
        log.info("Product Variant updated successfully, {}", updated);
        return updated;
    }


    //DELETE
    public void deletePriceListById(UUID priceListId) {
        if (!priceListsRepository.existsById(priceListId)) throw new NotFoundException("Price List not found");
        log.info("Price List deleted successfully, {}", priceListId);
        priceListsRepository.deleteById(priceListId);
    }

}
