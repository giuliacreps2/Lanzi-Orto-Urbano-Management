package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.AdminOrderDetailDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.CheckoutDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody @Validated CheckoutDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.ordersService.createOrderFromCart(body);
    }

    @PatchMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable UUID orderId) {
        this.ordersService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/admin-reorder")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void reorderOrder(@PathVariable UUID orderId, @RequestBody @Validated CheckoutDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        this.ordersService.findByIdAndReorderByAdmin(orderId, body);
    }

    @PatchMapping("/{orderId}/apply-loyalty")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Order applyLoyaltyDiscount(@PathVariable UUID orderId, @RequestBody @Validated CheckoutDTO body) {
        return this.ordersService.findByIdAndApplyLoyaltyDiscount(orderId, body);
    }

    //GET
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Order> findAllOrders(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        return this.ordersService.findAll(page, size, sortBy);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order findOrderById(@PathVariable UUID orderId) {
        return this.ordersService.findById(orderId);
    }


    //PATCH: cambio stato ordine
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Order updateOrderStatus(@PathVariable UUID orderId, @RequestParam StatusOrder statusOrder) {
        return this.ordersService.findByIdAndUpdateOrderStatus(orderId, statusOrder);
    }


    @GetMapping("/{orderId}/detail")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public AdminOrderDetailDTO findAdminOrderDetail(@PathVariable UUID orderId) {
        return this.ordersService.findAdminOrderDetailById(orderId);
    }


}
