package giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2bProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.B2cProfile;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.User;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.LoyaltyPoint;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.Order;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.orders.TrayReturn;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.orders.StatusOrder;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.BadRequestException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions.NotFoundException;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.login_signup.UsersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.LoyaltyPointsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.OrdersRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.repositories.orders.TrayReturnsRepository;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2bProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.B2cProfilesService;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.services.login_signup.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class LoyaltyPointsService {

    private static final long POINTS_PER_EURO = 100L;
    private final OrdersRepository ordersRepository;
    private final B2bProfilesService b2bProfilesService;
    private final B2cProfilesService b2cProfilesService;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final TrayReturnsService trayReturnsService;
    private final TrayReturnsRepository trayReturnsRepository;
    private final LoyaltyPointsRepository loyaltyPointsRepository;

    public LoyaltyPointsService(OrdersRepository ordersRepository, B2bProfilesService b2bProfilesService, B2cProfilesService b2cProfilesService, UsersService usersService, UsersRepository usersRepository, TrayReturnsService trayReturnsService, TrayReturnsRepository trayReturnsRepository, LoyaltyPointsRepository loyaltyPointsRepository) {
        this.ordersRepository = ordersRepository;
        this.b2bProfilesService = b2bProfilesService;
        this.b2cProfilesService = b2cProfilesService;
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.trayReturnsService = trayReturnsService;
        this.trayReturnsRepository = trayReturnsRepository;
        this.loyaltyPointsRepository = loyaltyPointsRepository;
    }


    public LoyaltyPoint awardPointsForOrder(UUID orderId, UUID userId) {
        //trova ordine per orderId, altrimenti NotFoundException

        Order foundOrder = this.ordersRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));

        //SE order.statusOrder != COMPLETED
        //    lancia BadRequestException "Punti assegnabili solo su ordini completati"

        if (foundOrder.getStatusOrder() != StatusOrder.COMPLETED) {
            throw new BadRequestException("Order has already been completed");
        }

        //puntiGuadagnati = converti order.totalAmount in punti
        //    (es. ogni 1€ = 10 punti, usa costante POINTS_PER_EURO)

        long pointsGained = this.convertDiscountToPoints(foundOrder.getTotalAmount());

        //recupera profilo tramite profileId e ProfileType
        //    SE B2C → b2cProfilesService.findById
        //    SE B2B → b2bProfilesService.findById
        //profilo.setLoyaltyPoints(profilo.getLoyaltyPoints() + puntiGuadagnati)
        //profilo.setLoyaltyLastActivity(now())
        //salva profilo


        User user = this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        B2cProfile b2cProfile = null;
        B2bProfile b2bProfile = null;

        if (user.getB2cProfile() != null) {
            B2cProfile profiloB2c = user.getB2cProfile();

            profiloB2c.setLoyaltyPoints(profiloB2c.getLoyaltyPoints() + pointsGained);
            profiloB2c.setLoyaltyLastActivity(LocalDateTime.now());

        } else if (user.getB2bProfile() != null) {
            B2bProfile profiloB2b = user.getB2bProfile();

            profiloB2b.setLoyaltyPoints(profiloB2b.getLoyaltyPoints() + pointsGained);
            profiloB2b.setLoyaltyLastActivity(LocalDateTime.now());
        } else {
            throw new BadRequestException("Invalid checkout request. You should be logged in");
        }

        this.usersRepository.save(user);


        //crea LoyaltyPoint con:
        //    - descrizione "Punti guadagnati per ordine completato"
        //    - order = ordine trovato
        //    - b2cProfile o b2bProfile in base a ProfileType
        //    - trayReturn = null
        //salva e ritorna

        LoyaltyPoint newLoyalPoint = LoyaltyPoint.builder()
                .descriptionLoyaltyPoints("POINTS GAINED for completed order")
                .order(foundOrder)
                .b2cProfile(b2cProfile)
                .b2bProfile(b2bProfile)
                .trayReturn(null)
                .build();

        log.info("Loyalty Points saved successfully: {}", newLoyalPoint);
        return this.loyaltyPointsRepository.save(newLoyalPoint);

    }


    public LoyaltyPoint awardPointsForTrayReturn(UUID trayReturnId, UUID userId, Long pointsAwarded) {

        //trova trayReturn per trayReturnId, altrimenti NotFoundException
        TrayReturn foundTrayReturn = this.trayReturnsRepository.findById(trayReturnId).orElseThrow(() -> new NotFoundException("TrayReturn not found"));

        //SE trayReturn.validatedByAdmin == false
        //    lancia BadRequestException "Il reso non è ancora stato validato"

        if (!foundTrayReturn.isValidatedByAdmin()) {
            throw new BadRequestException("Invalid checkout request. TrayReturn has not been validated");
        }

        //puntiGuadagnati = trayReturn.getPointsAwarded()

        //SE puntiGuadagnati <= 0
        //    lancia BadRequestException "Nessun punto da assegnare per questo reso"

        if (pointsAwarded <= 0) {
            throw new BadRequestException("Invalid checkout request. Loyalty Point is out of bounds");
        }

        //recupera profilo tramite profileId e ProfileType
        //profilo.setLoyaltyPoints(profilo.getLoyaltyPoints() + puntiGuadagnati)
        //profilo.setLoyaltyLastActivity(now())
        //salva profilo


        User user = this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        B2cProfile b2cProfile = null;
        B2bProfile b2bProfile = null;

        if (user.getB2cProfile() != null) {
            B2cProfile profiloB2c = user.getB2cProfile();

            profiloB2c.setLoyaltyPoints(profiloB2c.getLoyaltyPoints() + pointsAwarded);
            profiloB2c.setLoyaltyLastActivity(LocalDateTime.now());

        } else if (user.getB2bProfile() != null) {
            B2bProfile profiloB2b = user.getB2bProfile();

            profiloB2b.setLoyaltyPoints(profiloB2b.getLoyaltyPoints() + pointsAwarded);
            profiloB2b.setLoyaltyLastActivity(LocalDateTime.now());

        } else {
            throw new BadRequestException("Invalid checkout request. You should be logged in");
        }

        this.usersRepository.save(user);

        //crea LoyaltyPoint con:
        //    - descrizione "Punti guadagnati per reso vassoi"
        //    - trayReturn = trayReturn trovato
        //    - b2cProfile o b2bProfile in base a ProfileType
        //    - order = null
        //salva e ritorna

        LoyaltyPoint newLoyalPoint = LoyaltyPoint.builder()
                .descriptionLoyaltyPoints("POINTS GAINED for tray return validated ")
                .trayReturn(foundTrayReturn)
                .b2cProfile(b2cProfile)
                .b2bProfile(b2bProfile)
                .order(null)
                .build();

        log.info("Loyalty Points saved successfully from tray return: {}", newLoyalPoint);
        return this.loyaltyPointsRepository.save(newLoyalPoint);
    }


    public LoyaltyPoint redeemPoints(UUID userId, Long points) {

        //SE points <= 0
        //    lancia BadRequestException "Quantità punti non valida"
        if (points <= 0) {
            throw new BadRequestException("Points cannot be negative");
        }

        //recupera profilo tramite profileId e ProfileType
        //SE profilo.getLoyaltyPoints() < points
        //    lancia BadRequestException "Punti insufficienti"
        //
        //profilo.setLoyaltyPoints(profilo.getLoyaltyPoints() - points)
        //profilo.setLoyaltyLastActivity(now())
        //salva profilo

        User user = this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        B2cProfile b2cProfile = null;
        B2bProfile b2bProfile = null;

        if (user.getB2cProfile() != null) {
            B2cProfile profiloB2c = user.getB2cProfile();

            profiloB2c.setLoyaltyPoints(profiloB2c.getLoyaltyPoints() - points);
            profiloB2c.setLoyaltyLastActivity(LocalDateTime.now());


        } else if (user.getB2bProfile() != null) {
            B2bProfile profiloB2b = user.getB2bProfile();

            profiloB2b.setLoyaltyPoints(profiloB2b.getLoyaltyPoints() - points);
            profiloB2b.setLoyaltyLastActivity(LocalDateTime.now());


        } else {
            throw new BadRequestException("Invalid checkout request. You should be logged in");
        }

        this.usersRepository.save(user);


        //crea LoyaltyPoint con:
        //    - descrizione "Punti riscattati"
        //    - b2cProfile o b2bProfile in base a ProfileType
        //    - order = null
        //    - trayReturn = null
        //salva e ritorna sconto = convertPointsToDiscount(points)

        LoyaltyPoint newLoyalPoint = LoyaltyPoint.builder()
                .descriptionLoyaltyPoints("POINTS GAINED for completed order")
                .b2cProfile(b2cProfile)
                .b2bProfile(b2bProfile)
                .trayReturn(null)
                .build();

        log.info("Discount saved successfully: {}", newLoyalPoint);
        return this.loyaltyPointsRepository.save(newLoyalPoint);

    }


    public BigDecimal convertPointsToDiscount(Long points) {
        return BigDecimal.valueOf(points / POINTS_PER_EURO);
    }

    public Long convertDiscountToPoints(BigDecimal discount) {
        return discount.multiply(BigDecimal.valueOf(POINTS_PER_EURO)).longValue();
    }

    public void updateLoyalPoints() {
    }
}
//    public void expireOldLoyalPoints() {
//
//    }
//}
