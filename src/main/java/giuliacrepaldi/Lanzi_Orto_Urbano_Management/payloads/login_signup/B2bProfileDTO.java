package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup.Address;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;

public record B2bProfileDTO(
        String vatNumber,
        String fiscalCode,
        String contactPhone,
        String contactName,
        String contactSurname,
        String contactEmail,
        String companyName,
        TypeActivity typeActivity,
        Address legalAddress
) {
}
