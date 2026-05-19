package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.AccountType;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;

import java.util.List;
import java.util.UUID;

public record LoggedUserDTO(
        UUID userId,
        String email,
        List<String> roles,
        AccountType accountType,
        StatusB2b b2bStatus,
        boolean active,
        boolean emailVerified


) {
}
