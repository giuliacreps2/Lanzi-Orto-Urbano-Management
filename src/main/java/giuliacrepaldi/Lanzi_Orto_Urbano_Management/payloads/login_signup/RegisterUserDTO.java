package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.ClientCategory;
import jakarta.validation.constraints.*;

public record RegisterUserDTO(
        @NotBlank(message = "Your name is required")
        String name,
        @NotBlank(message = "Your surname is required")
        String surname,
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Your phoneNumber is not correct")
        String phoneNumber,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is not correct")
        String email,
        @Size(min = 8, message = "Your password must have at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Your password must have at least a capital, a lowercase")
        String password,
        @AssertTrue(message = "Privacy policy must be accepted to continue")
        boolean privacyAccepted,
        ClientCategory category
) {
}
