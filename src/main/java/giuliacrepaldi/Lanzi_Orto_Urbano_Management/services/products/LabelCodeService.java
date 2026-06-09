package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class LabelCodeService {

//    public String generateLabelCode(OrderItem item, int index) {
//        return "IT-"
//                + item.getOrder().getOrderId().toString().substring(0, 8)
//                + "-"
//                + item.getOrderItemId().toString().substring(0, 8)
//                + "-"
//                + (index + 1);
//
//    }

    public String generateLabelCode(OrderItem item, int index) {
        return "IT-"
                + item.getOrder().getOrderId().toString().substring(0, 8)
                + "-"
                + item.getOrderItemId().toString().substring(0, 8)
                + "-"
                + (index + 1);
    }

    public String generateEan13(OrderItem item, int index) {
        // Prefisso Italia GS1: 800
        String prefix = "800";

        // 9 cifre dal UUID dell'orderItem (solo cifre)
        String rawDigits = item.getOrderItemId().toString().replaceAll("[^0-9]", "");

        // Padding con zeri se le cifre non bastano
        String padded = (rawDigits + "000000000").substring(0, 9);

        // Indice per differenziare etichette dello stesso item (max 9)
        // Sostituiamo l'ultima cifra con l'indice (0-8)
        String nineDigits = padded.substring(0, 8) + (index % 9);

        String ean12 = prefix + nineDigits;

        // Calcolo checksum EAN-13
        int checksum = calculateEan13Checksum(ean12);

        return ean12 + checksum;
    }

    private int calculateEan13Checksum(String ean12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(ean12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        return (10 - (sum % 10)) % 10;
    }
}

