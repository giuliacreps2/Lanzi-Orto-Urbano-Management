package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.AccountType;
import jakarta.validation.constraints.*;

public record RegisterAccountUserDTO(
        @NotBlank(message = "Email is mandatory")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email is not correct")
        String email,
        @Size(min = 8, message = "Your password must have at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Your password must have at least a capital, a lowercase")
        String password,
        @AssertTrue(message = "Privacy policy must be accepted to continue")
        boolean privacyAccepted,
        AccountType intendedAccountType
) {
}
