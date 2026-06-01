package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;

import java.util.UUID;

public record LoyaltyPointDTO(
        UUID profileId,
        ClientCategory clientCategory,
        Long points,
        String description

) {
}
