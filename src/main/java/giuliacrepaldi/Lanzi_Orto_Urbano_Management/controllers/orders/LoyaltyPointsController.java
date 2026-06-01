package giuliacrepaldi.Lanzi_Orto_Urbano_Management.controllers.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.LoyaltyPoint;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.ValidationException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders.LoyaltyPointsService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loyalty-points")
public class LoyaltyPointsController {

    private final LoyaltyPointsService loyaltyPointsService;

    public LoyaltyPointsController(LoyaltyPointsService loyaltyPointsService) {
        this.loyaltyPointsService = loyaltyPointsService;
    }

    @PostMapping("/award/order/{orderId}/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LoyaltyPoint awardPointsForOrder(@PathVariable UUID orderId, @PathVariable UUID userId, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors()
                    .stream().map(FieldError::getDefaultMessage).toList();
            throw new ValidationException(errors);
        }
        return this.loyaltyPointsService.awardPointsForOrder(orderId, userId);
    }
}
