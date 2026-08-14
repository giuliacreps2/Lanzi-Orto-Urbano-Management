package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;

import java.util.UUID;

public record B2bProfileRespDTO(UUID b2bProfileId, StatusB2b statusB2b) {
}
