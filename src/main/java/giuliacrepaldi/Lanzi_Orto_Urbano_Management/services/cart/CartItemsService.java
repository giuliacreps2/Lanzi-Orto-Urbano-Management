package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.cart;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Cart;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.CartItem;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.products.ProductVariant;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartItemRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.cart.CartRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.products.ProductVariantsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.PriceListsService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.products.ProductVariantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemsService implements ICartItemService {

    private final CartRepository cartRepository;
    private final ProductVariantsRepository productVariantsRepository;
    private final ProductVariantsService productVariantsService;
    private final PriceListsService priceListsService;
    private final CartItemRepository cartItemRepository;
    private final CartsService cartsService;

    @Override
    public CartItem addItemToCart(Cart cart, UUID variantId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        ProductVariant productVariant = productVariantsService.findById(variantId);

        ClientCategory clientCategory;
        if (cart.getB2cProfile() != null || cart.getEmailWithoutAuthUser() != null) {
            clientCategory = ClientCategory.B2C;
        } else if (cart.getB2bProfile() != null) {
            clientCategory = ClientCategory.B2B;
        } else {
            throw new BadRequestException("Carrello non associato ad alcun profilo utente valido");
        }

        BigDecimal unitPrice = priceListsService.resolvePriceForVariant(
                productVariant.getVariantId(),
                clientCategory,
                quantity
        );

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProductVariantCartItem().getVariantId().equals(variantId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantityCartItem(cartItem.getQuantityCartItem() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariantCartItem(productVariant);
            cartItem.setQuantityCartItem(quantity);
            cartItem.setPriceSnapshot(unitPrice);
            cartItem.setAddedAt(LocalDateTime.now());

            cart.getItems().add(cartItem);
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        cartsService.refreshCartTotal(cart);

        return savedItem;
    }

    @Override
    public CartItem updateItemQuantity(UUID cartItemId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("CartItem not found"));

        cartItem.setQuantityCartItem(quantity);

        CartItem savedItem = cartItemRepository.save(cartItem);

        Cart parentCart = savedItem.getCart();
        cartsService.refreshCartTotal(parentCart);

        return savedItem;
    }


    @Override
    public void removeItemFromCart(Cart cart, UUID cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("CartItem not found"));

        cart.getItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        cartsService.refreshCartTotal(cart);

    }

    @Override
    public CartItem getCartItem(UUID cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Not found CartItem with id: " + cartItemId));
    }
}
