package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.AccountType;

import java.util.UUID;

public record NewUserRespDTO(UUID userId, AccountType intendedAccountType) {
}
