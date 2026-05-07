package giuliacrepaldi.Lanzi_Orto_Urbano_Management.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

    public NotFoundException(UUID id) {
        super("This id:" + id + "not found!");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
