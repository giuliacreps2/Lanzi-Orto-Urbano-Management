package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;
import jakarta.validation.constraints.AssertTrue;

public record RegisterB2bProfileDTO(
        String contactName,
        String contactSurname,
        String contactEmail,
        String contactPhone,
        String vatNumber,
        String fiscalCode,
        String companyName,
        String password,
        TypeActivity typeActivity,
        @AssertTrue(message = "Privacy policy must be accepted")
        boolean privacyAccepted
) {
}
