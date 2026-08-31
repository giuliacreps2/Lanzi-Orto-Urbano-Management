package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory;

public record StockAvailabilityResponse(
        boolean tracked,
        Integer availableQuantity
) {

}
