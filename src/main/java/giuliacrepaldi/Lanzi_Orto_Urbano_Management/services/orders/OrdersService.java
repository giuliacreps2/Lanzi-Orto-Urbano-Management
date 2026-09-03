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
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.inventory.StockAvailabilityResponse;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders.*;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.LoyaltyPointsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrderItemsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrdersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment.CreatedHostedOrdersReqRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment.NexiPaymentSessionRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.payment.NexiPaymentTransactionRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart.CartsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.inventory.InventoryService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.AuthService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2bProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2cProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.BatchesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.PriceListsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductVariantsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Service
public class OrdersService implements IOrdersService {

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
    private final CartsService cartsService;
    private final InventoryService inventoryService;
    private final CreatedHostedOrdersReqRepository createdHostedOrdersReqRepository;
    private final NexiPaymentSessionRepository nexiPaymentSessionRepository;
    private final NexiPaymentTransactionRepository nexiPaymentTransactionRepository;
    private final AuthService authService;
    private final CartsRepository cartsRepository;

    //1. CREAZIONE ORDINE DAL CARRELLO
//    @Transactional
//    @Override
//    public Order createOrderFromCart(CheckoutRequestDTO body) {
//
//        User currentUser = null;
//        boolean isGuest = true;
//
//        if (isGuest && (body.guestEmail() == null || body.guestEmail().isBlank())) {
//            throw new BadRequestException("Guest Email Required to Checkout Order");
//        }
//
//        Cart cart = isGuest
//                ? cartsService.getActiveCartByEmail(body.guestEmail())
//                : cartsService.getActiveCartByUserId(currentUser);
//
//        if (cart.getItems() == null || cart.getItems().isEmpty()) {
//            throw new BadRequestException("No items in order");
//        }
//
//
//        for (CartItem cartItem : cart.getItems()) {
//            UUID variantId = cartItem.getProductVariantCartItem().getVariantId();
//
//            StockAvailabilityResponse stock = inventoryService.getAvailableQuantity(variantId);
//
//            if (!stock.tracked() || stock.availableQuantity() < cartItem.getQuantityCartItem()) {
//                throw new BadRequestException("Giacenza insufficiente per: "
//                        + variantId);
//            }
//        }
//
//
//        B2cProfile b2c = isGuest ? null : currentUser.getB2cProfile();
//        B2bProfile b2b = isGuest ? null : currentUser.getB2bProfile();
//
//        if (!isGuest && b2c == null && b2b == null) {
//            throw new BadRequestException("Invalid checkout request. User profile not found");
//        }
//
//        //1. Salva Ordine PENDING
//        Order newOrder = Order.builder()
//                .statusOrder(StatusOrder.PENDING)
//                .sourceOrder(SourceOrder.CUSTOMER_SELF)
//                .deliveryType(body.deliveryType())
//                .reorderedFormByAdmin(false)
//                .orderCreatedAt(LocalDateTime.now())
//                .loyaltyPointsUsed(false)
//                .totalAmount(BigDecimal.ZERO)
//                .discountAmount(BigDecimal.ZERO)
//                .paymentMethod(body.paymentMethod())
//                .b2cProfile(b2c)
//                .b2bProfile(b2b)
//                .guestEmail(isGuest ? body.guestEmail() : null)
//                .guestName(isGuest ? body.guestName() : null)
//                .build();
//
//        List<OrderItem> orderItems = new ArrayList<>();
//
//
//        for (CartItem cartItem : cart.getItems()) {
//            inventoryService.releaseStock(cartItem.getProductVariantCartItem().getVariantId(), cartItem.getQuantityCartItem());
//        }
//
//        BigDecimal total = BigDecimal.ZERO;
//        for (CartItem cartItem : cart.getItems()) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(newOrder);
//            orderItem.setProductVariant(cartItem.getProductVariantCartItem());
//            orderItem.setQuantity(cartItem.getQuantityCartItem());

    /// /          orderItem.setPriceSnapshot(cartItem.getPriceSnapshot());
//        }
//
//        newOrder.setTotalAmount(BigDecimal.valueOf(total.longValueExact()));
//
//        Order orderPending = this.ordersRepository.save(newOrder);
//
//
//        //2.Richiesta di avviare il pagamento
//        CreatedHostedOrderRequest request = CreatedHostedOrderRequest.builder()
//                .createdAt(LocalDateTime.now())
//                .order(orderPending)
//                .build();
//
//        this.createHostedOrdersReqRepository.save(request);
//
//        //3.Avvio sessione Pagamento
//        nextPaymentSession.initialize(request);
//
//        NexiPaymentSession session = NexiPaymentSession.builder()
//                .sessionStatus(SessionStatus.CREATED)
//                .createdAt(LocalDateTime.now())
//                .language("ita")
//                .cancelUrl("")
//                .hostedPageUrl("")
//                .resultUrl("")
//                .order(orderPending)
//                .securityTokenSession()
//                .build();
//
//        nexiPaymentSessionRepository.save(session);
//
//        //4. Avvio la sessione di Transazione
//
//        NexiPaymentTransaction transaction = NexiPaymentTransaction.builder()
//                .amount(orderPending.getTotalAmount(BigDecimal.valueOf(total.longValueExact())))
//                .createdAt(LocalDateTime.now())
//                .currency("EUR")
//                .nextPaymentSession(session)
//                .paymentStatus(PaymentStatus.SUCCESS)
//                .paymentInstrumentInfo("")
//                .build();
//
//        nexiPaymentTransactionRepository.save(transaction);
//
//
//        //5. Ordine passa da Pending a Completato
//
//        newOrder.setStatusOrder(StatusOrder.COMPLETED);
//
//        //6. Scarico dal magazzino
//
//
//        Order savedOrder = this.ordersRepository.save(newOrder);
//
//        return savedOrder;
//    }


//    public CheckoutInitResponse createOrderFromCart(CheckoutRequestDTO body, User currentUser) {
//        boolean isGuest = true;
//
//        if (isGuest && (body.guestEmail() == null || body.guestEmail().isBlank())) {
//            throw new BadRequestException("Guest Email Required to Checkout Order");
//        }
//
//        Cart cart = isGuest
//                ? cartsService.getActiveCartByEmail(body.guestEmail())
//                : cartsService.getActiveCartByUserId(currentUser);
//    }


    //PRIVATE METHODS TO COMPLETE ORDER AND PAYMENT

    //
//    private Cart resolveActiveCart(CheckoutRequestDTO body, User currentUser) {
//        return null;
//    }


    //-----------------//

    //2. INIZIALIZZARE IL PAGAMENTO
//    public PaymentResponse initPayment(CheckoutRequestDTO body) {
//
//        //Salvo l'ordine prima di interagire con il gateway
//        Order orderPending = createAndSavePendingOrder(body);
//
//        //Preparo la chiamata Nexi usando l'ID dell'Ordine salvato
//        CreatedHostedOrderRequest request = CreatedHostedOrderRequest.builder()
//                .createdAt(LocalDateTime.now())
//                .order(orderPending)
//
//                .build();
//
//        // 3. Esegui la chiamata a Nexi per ottenere l'URL di pagamento
//        return nextPaymentSession.initialize(request);
//    }

    //3.


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

    public List<OrderSummaryDTO> findByUser(User currentUser) {
        List<Order> orders;

        if (currentUser.getB2bProfile() != null) {
            orders = this.ordersRepository
                    .findByB2bProfile_B2bProfileIdOrderByOrderCreatedAtDesc(
                            currentUser.getB2bProfile().getB2bProfileId()
                    );
        } else {
            orders = this.ordersRepository.findByB2cProfile_B2cProfileIdOrderByOrderCreatedAtDesc(
                    currentUser.getB2cProfile().getB2cProfileId());
        }

        return orders.stream().limit(5).map(order -> new OrderSummaryDTO(
                order.getOrderId(),
                "ORD-" + order.getOrderId().toString().substring(0, 8).toUpperCase(),
                order.getStatusOrder(),
                order.getOrderCreatedAt(),
                order.getTotalAmount(),
                order.getItems().stream().map(item -> new OrderItemSummaryDTO(
                        item.getProductVariant().getProduct().getProductName(),
                        item.getQuantity()
                )).toList()
        )).toList();
    }

    @Transactional
    @Override
    public Order createOrderFromCart(User currentUser, CheckoutRequestDTO body) {

        boolean isGuest = currentUser == null;

        if (isGuest && (body.guestEmail() == null || body.guestEmail().isBlank())) {
            throw new BadRequestException("Guest email is required to checkout");
        }

        //Recuperiamo il carrello
        Cart cart = cartsService.getCartForCheckout(body.cartId(), currentUser, body.guestEmail());

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("No items in order");
        }


        //Verifichiamo la disponibilità degli oggetti nel carrello
        for (CartItem cartItem : cart.getItems()) {

            UUID variantId = cartItem.getProductVariantCartItem().getVariantId();

            StockAvailabilityResponse stock = inventoryService.getAvailableQuantity(variantId);

            if (!stock.tracked() || stock.availableQuantity() < cartItem.getQuantityCartItem()) {
                throw new BadRequestException("Insufficient quantity" + variantId);
            }
        }

        //Calcolo il totale
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            BigDecimal unitPrice = cartItem.getPriceSnapshot();

            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantityCartItem()));

            total = total.add(itemTotal);
        }

        long totalAmount = total.movePointRight(2).longValueExact();


        //Recupero dell'utente
        B2cProfile b2cProfile = null;
        B2bProfile b2bProfile = null;

        if (currentUser != null) {
            b2cProfile = cart.getB2cProfile();
            b2bProfile = cart.getB2bProfile();
        }

        //Creazione dell'ordine con stato Pending
        Order newOrder = Order.builder()
                .statusOrder(StatusOrder.PENDING)
                .sourceOrder(SourceOrder.CUSTOMER_SELF)
                .deliveryType(body.deliveryType())
                .reorderedFormByAdmin(false)
                .orderCreatedAt(LocalDateTime.now())
                .loyaltyPointsUsed(false)
                .totalAmount(total)
                .discountAmount(BigDecimal.ZERO)
                .paymentMethod(body.paymentMethod())
                .b2cProfile(b2cProfile)
                .b2bProfile(b2bProfile)
                .guestEmail(isGuest ? body.guestEmail() : null)
                .guestName(isGuest ? body.guestName() : null)
                .build();

        Order savedOrder = ordersRepository.save(newOrder);

        //Trasformo gli articoli del carrello negli articoli dell'ordine
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProductVariant(cartItem.getProductVariantCartItem());
            orderItem.setQuantity(cartItem.getQuantityCartItem());
            orderItem.setPrice(cartItem.getPriceSnapshot());

            orderItems.add(orderItem);
        }

        orderItemsRepository.saveAll(orderItems);

        return savedOrder;
    }


}


