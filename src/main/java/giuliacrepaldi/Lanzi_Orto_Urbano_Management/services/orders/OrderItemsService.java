package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrderItemsRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;

    public OrderItemsService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }


//    public OrderItem saveNewItem(OrderItemDTO body) {
//
//    }
}
