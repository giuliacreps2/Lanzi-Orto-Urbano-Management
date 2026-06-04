package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.Batch;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.SourceOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusDeliveryType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.LoyaltyPointsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrderItemsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrdersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2bProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2cProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.BatchesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.PriceListsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductVariantsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final B2cProfilesService b2cProfilesService;
    private final B2bProfilesService b2bProfilesService;
    private final ProductVariantsService productVariantsService;
    private final BatchesService batchesService;
    private final LoyaltyPointsService loyaltyPointsService;
    private final LoyaltyPointsRepository loyaltyPointsRepository;
    private final PriceListsService priceListsService;
    private final UsersService usersService;


    public OrdersService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, B2cProfilesService b2cProfilesService, B2bProfilesService b2bProfilesService, ProductVariantsService productVariantsService, BatchesService batchesService, LoyaltyPointsService loyaltyPointsService, LoyaltyPointsRepository loyaltyPointsRepository, PriceListsService priceListsService, UsersService usersService) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.b2cProfilesService = b2cProfilesService;
        this.b2bProfilesService = b2bProfilesService;
        this.productVariantsService = productVariantsService;
        this.batchesService = batchesService;
        this.loyaltyPointsService = loyaltyPointsService;
        this.loyaltyPointsRepository = loyaltyPointsRepository;
        this.priceListsService = priceListsService;
        this.usersService = usersService;
    }


    //CREATE
    @Transactional
    public Order createOrderFromCart(User currentUser, CheckoutDTO body) {

        //PROFILO
        B2cProfile b2c = null;
        B2bProfile b2b = null;
        ClientCategory clientCategory;

        if (currentUser.getB2cProfile() != null) {
            b2c = currentUser.getB2cProfile();
            clientCategory = ClientCategory.B2C;
        } else if (currentUser.getB2bProfile() != null) {
            b2b = currentUser.getB2bProfile();
            clientCategory = ClientCategory.B2B;
        } else {
            throw new BadRequestException("Invalid checkout request. User profile not found");
        }

        Order order = Order.builder()
                .statusOrder(StatusOrder.PENDING)
                .deliveryType(body.deliveryType())
                .sourceOrder(SourceOrder.CUSTOMER_SELF)
                .reorderedFormByAdmin(false)
                .orderCreatedAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .loyaltyPointsUsed(body.loyaltyPointsUsed())
                .b2cProfile(b2c)
                .b2bProfile(b2b)
                .build();

//LISTA ORDINI
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        Batch firstAvailableBatch = null;

        for (OrderItemDTO cartItem : body.items()) {
            ProductVariant variant = this.productVariantsService.findById(cartItem.variantId());

            BigDecimal unitPrice = this.priceListsService.resolvePriceForVariant(
                    variant.getVariantId(), clientCategory, cartItem.quantity());

            BigDecimal itemTotalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.quantity()));
            totalOrderAmount = totalOrderAmount.add(itemTotalPrice);

            Batch availableBatch = this.batchesService.findAvailableBatchForVariant(variant.getVariantId());
            if (firstAvailableBatch == null) firstAvailableBatch = availableBatch;

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productVariant(variant)
                    .quantity(cartItem.quantity())
                    .price(unitPrice)
                    .batch(availableBatch)
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalOrderAmount);


        //PAYMENT
        PaymentMethod payment = PaymentMethod.builder()
                .paymentType(body.paymentType())
                .billingDetails(body.billingDetails())
                .build();
        order.setPaymentMethod(payment);


        //DELIVERY
        String recipientName;
        if (b2c != null) {
            recipientName = b2c.getName();
        } else {
            assert b2b != null;
            recipientName = b2b.getContactName() + " " + b2b.getContactSurname();
        }

        LocalDateTime deliveryDate = firstAvailableBatch != null ? firstAvailableBatch.getExpectedHarvestDate().atStartOfDay().plusDays(2)
                : LocalDateTime.now().plusDays(10);

        Delivery newDelivery = Delivery.builder()
                .trackingNumber(String.valueOf(UUID.randomUUID()))
                .statusDeliveryType(StatusDeliveryType.PENDING)
                .deliveryDate(deliveryDate)
                .recipientName(recipientName)
                .shippingAddress(body.billingDetails())
                //COSTO SPEDIZIONE DA CALCOLARE
                .priceDelivery(BigDecimal.ZERO)
                .driver(null)
                .b2cProfile(b2c)
                .b2bProfile(b2b)
                .build();

        order.setDelivery(newDelivery);

        //LOYALTY POINTS

        if (body.loyaltyPointsUsed()) {
            Long availablePoints = b2c != null ? b2c.getLoyaltyPoints() : b2b.getLoyaltyPoints();

            if (availablePoints <= 0) {
                throw new BadRequestException("Loyalty Points not available");
            }

            BigDecimal discount = this.loyaltyPointsService.convertPointsToDiscount(availablePoints);

            if (discount.compareTo(totalOrderAmount) > 0) {
                discount = totalOrderAmount;
            }

            order.setTotalAmount(totalOrderAmount.subtract(discount));
            order.setLoyaltyDiscount(BigDecimal.valueOf(discount.doubleValue()));

            Long pointsUsed = this.loyaltyPointsService.convertDiscountToPoints(discount);
            if (b2c != null) {
                b2c.setLoyaltyPoints(b2c.getLoyaltyPoints() - (pointsUsed));
                b2c.setLoyaltyLastActivity(LocalDateTime.now());
                this.b2cProfilesService.save(b2c);
            } else {
                b2b.setLoyaltyPoints(b2b.getLoyaltyPoints() - (pointsUsed));
                b2b.setLoyaltyLastActivity(LocalDateTime.now());
                this.b2bProfilesService.save(b2b);
            }

            //LOYALTYPOINTS NUOVI
            LoyaltyPoint newLoyalPoints = LoyaltyPoint.builder()
                    .descriptionLoyaltyPoints("Points gain from new Order")
                    .order(order)
                    .b2cProfile(order.getB2cProfile())
                    .b2bProfile(order.getB2bProfile())
                    .trayReturn(null)
                    .build();

            loyaltyPointsRepository.save(newLoyalPoints);


        }

        Order savedOrder = ordersRepository.save(order);
        log.info("Order saved successfully with id: {}", savedOrder.getOrderId());
        return savedOrder;
    }


    //CALCULATE TOTAL AMOUNT
    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;

        return items.stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    //UPDATE
    public Order findByIdAndUpdateOrderStatus(UUID orderId, StatusOrder newStatus) {
        Order found = this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        found.setStatusOrder(newStatus);
        found.setOrderUpdatedAt(LocalDateTime.now());

        this.ordersRepository.save(found);
        log.info("Order has been updated successfully: {}", found);
        return found;
    }


    //DELETE
    @Transactional
    public void cancelOrder(UUID orderId) {
        Order found = this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        if (found.getStatusOrder() == StatusOrder.CANCELLED || found.getStatusOrder() == StatusOrder.COMPLETED)
            throw new BadRequestException("Operation denied: order already cancelled or completed");

        if (found.isLoyaltyPointsUsed()) {
            long pointsToRestore = this.loyaltyPointsService.convertDiscountToPoints(found.getLoyaltyDiscount());

            if (found.getB2cProfile() != null) {
                B2cProfile b2cProfile = found.getB2cProfile();
                b2cProfile.setLoyaltyPoints(b2cProfile.getLoyaltyPoints() + pointsToRestore);
                b2cProfile.setLoyaltyLastActivity(LocalDateTime.now());
                this.b2cProfilesService.save(b2cProfile);
            } else if (found.getB2bProfile() != null) {
                B2bProfile b2bProfile = found.getB2bProfile();
                b2bProfile.setLoyaltyPoints(b2bProfile.getLoyaltyPoints() + pointsToRestore);
                b2bProfile.setLoyaltyLastActivity(LocalDateTime.now());
                this.b2bProfilesService.save(b2bProfile);
            }
            found.setLoyaltyPointsUsed(false);
            found.setLoyaltyDiscount(null);
        }

        found.setStatusOrder(StatusOrder.CANCELLED);
        found.setOrderUpdatedAt(LocalDateTime.now());
        this.ordersRepository.save(found);
    }

    @Transactional
    public void findByIdAndReorderByAdmin(UUID orderId, User currentUser, CheckoutDTO body) {
        B2bProfile foundB2b = currentUser.getB2bProfile();
        Order found = this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        if (!foundB2b.getAutoReorderEnabled()) throw new BadRequestException("Operation denied: you can't do reorder");
        if (found.getStatusOrder() != StatusOrder.COMPLETED && found.getStatusOrder() != StatusOrder.CANCELLED) {
            throw new BadRequestException("Operation denied: order must be completed or cancelled to reorder");
        }

        Order newOrder = Order.builder()
                .b2bProfile(foundB2b)
                .deliveryType(body.deliveryType())
                .statusOrder(StatusOrder.PENDING)
                .sourceOrder(SourceOrder.ADMIN_MANUAL)
                .reorderedFormByAdmin(true)
                .loyaltyPointsUsed(false)
                .totalAmount(BigDecimal.ZERO)
                .build();

        List<OrderItem> newOrderItems = new ArrayList<>();
        BigDecimal newTotalOrderAmount = BigDecimal.ZERO;
        Batch firstAvailableBatch = null;

        for (OrderItemDTO cartItem : body.items()) {
            ProductVariant variant = this.productVariantsService.findById(cartItem.variantId());
            BigDecimal unitPrice = this.priceListsService.resolvePriceForVariant(
                    variant.getVariantId(), ClientCategory.B2B, cartItem.quantity());

            BigDecimal itemTotalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.quantity()));
            newTotalOrderAmount = newTotalOrderAmount.add(itemTotalPrice);

            Batch availableBatch = this.batchesService.findAvailableBatchForVariant(variant.getVariantId());
            if (firstAvailableBatch == null) firstAvailableBatch = availableBatch;

            OrderItem orderItem = OrderItem.builder()
                    .order(newOrder)
                    .productVariant(variant)
                    .quantity(cartItem.quantity())
                    .price(unitPrice)
                    .batch(availableBatch)
                    .build();

            newOrderItems.add(orderItem);
        }

        newOrder.setItems(newOrderItems);
        newOrder.setTotalAmount(newTotalOrderAmount);

        PaymentMethod payment = PaymentMethod.builder()
                .paymentType(body.paymentType())
                .billingDetails(body.billingDetails())
                .build();
        newOrder.setPaymentMethod(payment);

        LocalDateTime deliveryDate = firstAvailableBatch != null ? firstAvailableBatch.getExpectedHarvestDate().atStartOfDay().plusDays(2)
                : LocalDateTime.now().plusDays(10);

        Delivery newDelivery = Delivery.builder()
                .trackingNumber(String.valueOf(UUID.randomUUID()))
                .statusDeliveryType(StatusDeliveryType.PENDING)
                .deliveryDate(deliveryDate)
                .recipientName(foundB2b.getContactName() + " " + foundB2b.getContactSurname())
                .shippingAddress(body.billingDetails())
                .priceDelivery(BigDecimal.ZERO)
                .b2bProfile(foundB2b)
                .build();

        newOrder.setDelivery(newDelivery);
        this.ordersRepository.save(newOrder);
    }

    @Transactional
    public Order findByIdAndApplyLoyaltyDiscount(UUID orderId, User currentUser) {
        Order found = this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        if (found.getStatusOrder() != StatusOrder.PENDING)
            throw new BadRequestException("You can't apply discount to this order because it is not PENDING");
        if (found.isLoyaltyPointsUsed())
            throw new BadRequestException("Discount already applied");

        Long availablePoints = 0L;
        if (currentUser.getB2cProfile() != null) {
            B2cProfile b2c = currentUser.getB2cProfile();
            availablePoints = b2c.getLoyaltyPoints();
        } else if (currentUser.getB2bProfile() != null) {
            B2bProfile b2b = currentUser.getB2bProfile();
            availablePoints = b2b.getLoyaltyPoints();
        }

        if (availablePoints <= 0) {
            throw new BadRequestException("Loyalty Points not available");
        }

        BigDecimal discount = this.loyaltyPointsService.convertPointsToDiscount(availablePoints);
        if (discount.compareTo(found.getTotalAmount()) > 0) {
            discount = found.getTotalAmount();
        }

        found.setTotalAmount(found.getTotalAmount().subtract(discount));
        found.setLoyaltyDiscount(BigDecimal.valueOf(discount.doubleValue()));
        found.setLoyaltyPointsUsed(true);

        return this.ordersRepository.save(found);
    }

    public Order findById(UUID orderId) {
        return this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public Page<Order> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return this.ordersRepository.findAll(pageable);
    }


    public Order startProcessingOrder(UUID orderId) {
        Order found = this.findById(orderId);

        if (found.getStatusOrder() == StatusOrder.CANCELLED) {
            throw new BadRequestException("Cannot start production for a cancelled order");
        }

        if (found.getStatusOrder() == StatusOrder.COMPLETED) {
            throw new BadRequestException("Cannot start production for a completed order");
        }

        found.setStatusOrder(StatusOrder.PROCESSING);
        found.setOrderCreatedAt(LocalDateTime.now());

        return this.ordersRepository.save(found);
    }


    public AdminOrderDetailDTO findAdminOrderDetailById(UUID orderId) {
        Order found = this.findById(orderId);

        AdminOrderCustomerDTO customerDTO;

        if (found.getB2cProfile() != null) {
            customerDTO = new AdminOrderCustomerDTO(
                    "B2C",
                    found.getB2cProfile().getName() + " " + found.getB2cProfile().getSurname(),
                    found.getB2cProfile().getUser() != null ? found.getB2cProfile().getUser().getEmail() : null,
                    found.getB2cProfile().getPhoneNumber(),
                    null,
                    null
            );
        } else {
            customerDTO = new AdminOrderCustomerDTO(
                    "B2B",
                    found.getB2bProfile().getContactName() + " " + found.getB2bProfile().getContactSurname(),
                    found.getB2bProfile().getContactEmail(),
                    found.getB2bProfile().getContactPhone(),
                    found.getB2bProfile().getCompanyName(),
                    null
            );
        }

        AdminOrderDeliveryDTO deliveryDTO = new AdminOrderDeliveryDTO(
                found.getDelivery().getRecipientName(),
                found.getDelivery().getDeliveryDate(),
                found.getDelivery().getStatusDeliveryType().name(),
                found.getDelivery().getShippingAddress()
        );

        List<AdminOrderItemDTO> itemsDTOs = found.getItems().stream()
                .map(item -> {
                    UUID batchId = (item.getBatch() != null) ? item.getBatch().getBatchId() : null;
                    String batchCode = (item.getBatch() != null) ? item.getBatch().getBatchCode() : null;
                    LocalDate expectedHarvestDate = (item.getBatch() != null) ? item.getBatch().getExpectedHarvestDate() : null;

                    String categoryName = null;
                    if (item.getProductVariant() != null && item.getProductVariant().getProduct() != null
                            && item.getProductVariant().getProduct().getProductCategory() != null) {
                        categoryName = item.getProductVariant().getProduct().getProductCategory().getNameProdCategory();
                    }


                    return new AdminOrderItemDTO(
                            item.getOrderItemId(),
                            item.getQuantity(),
                            item.getPrice(),

                            item.getProductVariant().getProduct().getProductName(),
                            item.getProductVariant().getProduct().getProductCategory().getNameProdCategory(),
                            item.getProductVariant().getVariantId(),
                            item.getProductVariant().getSkuVariant(),
                            item.getProductVariant().getNetWeight(),
                            item.getProductVariant().getUnit().name(),

                            batchId,
                            batchCode,
                            expectedHarvestDate,

                            item.getProductVariant().getTechnicalDetails(),
                            item.getLabels() != null ? item.getLabels().size() : 0
                    );
                })
                .toList();

        return new AdminOrderDetailDTO(
                found.getOrderId(),
                "ORD-" + found.getOrderId().toString().substring(0, 8).toUpperCase(),
                found.getStatusOrder(),
                found.getSourceOrder(),
                found.getDeliveryType(),
                found.getOrderCreatedAt(),
                found.getOrderUpdatedAt(),
                found.getTotalAmount(),
                found.getOrderNotes(),
                customerDTO,
                deliveryDTO,
                itemsDTOs
        );

    }
}


