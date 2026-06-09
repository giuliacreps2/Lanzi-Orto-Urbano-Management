package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Order createOrder(@AuthenticationPrincipal User currentUser, @RequestBody @Validated CheckoutDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        return this.ordersService.createOrderFromCart(currentUser, body);
    }

    @PatchMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable UUID orderId) {
        this.ordersService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/admin-reorder")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void reorderOrder(@PathVariable UUID orderId, @AuthenticationPrincipal User currentUser, @RequestBody @Validated CheckoutDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        this.ordersService.findByIdAndReorderByAdmin(orderId, currentUser, body);
    }

    @PatchMapping("/{orderId}/apply-loyalty")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Order applyLoyaltyDiscount(@PathVariable UUID orderId, @AuthenticationPrincipal User currentUser) {
        return this.ordersService.findByIdAndApplyLoyaltyDiscount(orderId, currentUser);
    }

    //GET
//    @GetMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public Page<Order> findAllOrders(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "10") int size,
//                                     @RequestParam(defaultValue = "orderId") String sortBy) {
//        if (size > 100 || size < 0) size = 10;
//        if (page < 0) page = 0;
//        return this.ordersService.findAll(page, size, sortBy);
//    }


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<AdminOrderDetailDTO> findAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderId") String sortBy
    ) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Page<Order> ordersEntityPage = this.ordersService.findAll(page, size, sortBy);

        Page<AdminOrderDetailDTO> dtoPage = ordersEntityPage.map(order -> {

            return new AdminOrderDetailDTO(
                    order.getOrderId(),
                    "ORD-" + order.getOrderId().toString().substring(0, 8).toUpperCase(),
                    order.getStatusOrder(),
                    order.getSourceOrder(),
                    order.getDeliveryType(),
                    order.getOrderCreatedAt(),
                    order.getOrderUpdatedAt(),
                    order.getTotalAmount(),
                    order.getOrderNotes(),
                    mapCustomerDetails(order),
                    null,

                    order.getItems().stream().map(item -> new AdminOrderItemDTO(
                            item.getOrderItemId(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getProductVariant().getProduct().getProductName(),
                            null, item.getProductVariant().getVariantId(), null, null, null, null, null, null, null, null
                    )).toList()
            );
        });
        return dtoPage;
    }

    private AdminOrderCustomerDTO mapCustomerDetails(Order order) {
        if (order.getB2cProfile() != null) {
            String fullName = order.getB2cProfile().getName() + " " + order.getB2cProfile().getSurname();
            return new AdminOrderCustomerDTO(
                    "B2C",
                    fullName.trim(),
                    order.getB2cProfile().getUser().getEmail(),
                    order.getB2cProfile().getPhoneNumber(),
                    null,
                    null
            );
        }

        if (order.getB2bProfile() != null) {
            String fullContactName = order.getB2bProfile().getContactName() + " " + order.getB2bProfile().getContactSurname();
            return new AdminOrderCustomerDTO(
                    "B2B",
                    order.getB2bProfile().getCompanyName(),
                    order.getB2bProfile().getContactEmail(),
                    order.getB2bProfile().getContactPhone(),
                    fullContactName.trim(),
                    null
            );
        }
        return new AdminOrderCustomerDTO("B2C", "Cliente Ospite", null, null, null, null);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderSummaryDTO> getMyOrders(@AuthenticationPrincipal User currentUser) {
        return this.ordersService.findByUser(currentUser);
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
