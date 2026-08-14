package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CompleteB2bProfileDTO(
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
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Pec is not correct")
        String pec,
        @Pattern(regexp = "^[A-Za-z0-9]{7}$", message = "SDI is not valid")
        String sdiCode,
        @NotBlank(message = "Company name is required")
        String companyName,
        @Email(message = "Contact email is not valid")
        String contactEmail,
        @NotBlank(message = "Contact phone is required")
        @Pattern(
                regexp = "^\\+?[0-9]{10,15}$",
                message = "Contact phone is not valid"
        )
        String contactPhone,
        @NotNull(message = "Type activity is required")
        TypeActivity typeActivity,

        @NotNull(message = "Address is required")
        @Valid
        AddressDTO addressDTO
) {
    @AssertTrue(message = "VAT number or fiscal code code is required")
    public boolean isVatNumberOrFiscalCodePresent() {
        return (vatNumber != null && !vatNumber.isBlank()) || (fiscalCode != null && !fiscalCode.isBlank());
    }

    public record AddressDTO(
            @NotBlank String street,
            @NotNull Integer number,
            @NotBlank String locality,
            @NotBlank
            @Pattern(regexp = "^[0-9]{5}$", message = "Postal code must contain 5 digits")
            String postalCode,
            @NotNull UUID municipalityId
    ) {
    }
}