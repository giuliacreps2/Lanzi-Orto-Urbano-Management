package giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("In attesa di elaborazione"),                        // In attesa di elaborazione
    REQUIRES_ACTION("Richiesta autenticazione dell'utente"),                // Richiede azione utente (3DS)
    FAILED("Transazione fallita"),                      // Fallito
    SUCCESS("Transazione avvenuta con successo"),                  // Completato con successo
    REFUNDED("Transazione rimborsata"),               // Rimborsato
    CANCELED("Transazione annullata");             // Annullato dal cliente/sistema ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
