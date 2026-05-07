package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime now) {
}

