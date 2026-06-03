package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.OrderItem;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrderItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;

    public OrderItemsService(OrderItemsRepository orderItemsRepository) {
        this.orderItemsRepository = orderItemsRepository;
    }

    public List<OrderItem> findAllByOrderId(UUID orderId) {
        return this.orderItemsRepository.findByOrder_OrderId(orderId);
    }


//    public OrderItem saveNewItem(OrderItemDTO body) {
//
//    }
}
