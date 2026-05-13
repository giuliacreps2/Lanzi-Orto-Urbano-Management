package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.login_signup;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsWithListDTO(String message, LocalDateTime timestamp, List<String> errors) {
}
