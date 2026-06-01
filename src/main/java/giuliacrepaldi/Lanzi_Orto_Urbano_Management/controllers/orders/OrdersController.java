package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.CheckoutDTO;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders.OrdersService;
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

}
