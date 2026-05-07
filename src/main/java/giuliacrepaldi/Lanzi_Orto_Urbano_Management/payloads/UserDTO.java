package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UserDTO(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is not correct")
        String email,
        @NotBlank(message = "Your password is mandatory")
        @Size(min = 8, message = "Your password must have at least 8 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Your password must have at least a capital, a lowercase")
        String password,
        @Past
        LocalDateTime createdAt
) {
}
