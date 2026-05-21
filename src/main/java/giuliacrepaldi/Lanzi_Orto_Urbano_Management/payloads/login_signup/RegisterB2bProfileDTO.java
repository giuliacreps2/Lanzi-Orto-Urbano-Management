package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record RegisterB2bProfileDTO(
        @NotBlank(message = "Contact name is required")
        String contactName,
        @NotBlank(message = "Contact surname is required")
        String contactSurname,
        @NotBlank(message = "Contact email is required")
        @Email(message = "Contact email is not valid")
        String contactEmail,
        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Contact phone is not valid"
        )
        String contactPhone,
        @NotBlank(message = "VAT number is required")
        @Pattern(
                regexp = "^[0-9]{11}$",
                message = "VAT number must contain 11 digits"
        )
        String vatNumber,
        @Pattern(
                regexp = "^$|^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$|^[0-9]{11}$",
                message = "Fiscal code is not valid"
        )
        String fiscalCode,
        @NotBlank(message = "Company name is required")
        String companyName,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must have at least 8 characters")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter and one number"
        )
        String password,
        @NotNull(message = "Type activity is required")
        TypeActivity typeActivity,
        UUID municipalityId,
        @AssertTrue(message = "Privacy policy must be accepted")
        boolean privacyAccepted
) {
    @AssertTrue(message = "VAT number or fiscal code code is required")
    public boolean isVatNumberOrFiscalCodePresent() {
        return (vatNumber != null && !vatNumber.isBlank()) || (fiscalCode != null && !fiscalCode.isBlank());
    }
}
