package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import java.time.LocalDate;

public record B2cProfileDTO(
        String name,
        String surname,
        String phoneNumber,
        LocalDate birthDate,
        String avatar
) {
}
