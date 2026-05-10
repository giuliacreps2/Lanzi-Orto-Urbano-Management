package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import jakarta.validation.constraints.AssertTrue;

public record RegisterUserDTO(
        String name,
        String surname,
        String phoneNumber,
        String email,
        String password,
        @AssertTrue(message = "Privacy policy must be accepted")
        boolean privacyAccepted
) {
}
