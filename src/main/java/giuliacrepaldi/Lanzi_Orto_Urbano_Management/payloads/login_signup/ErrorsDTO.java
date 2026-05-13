package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime now) {
}

