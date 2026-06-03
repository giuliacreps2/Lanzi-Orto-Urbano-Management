package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class LabelCodeService {

    public String generateLabelCode(OrderItem item, int index) {
        return "IT-"
                + item.getOrder().getOrderId().toString().substring(0, 8)
                + "-"
                + item.getOrderItemId().toString().substring(0, 8)
                + "-"
                + (index + 1);

    }
}
