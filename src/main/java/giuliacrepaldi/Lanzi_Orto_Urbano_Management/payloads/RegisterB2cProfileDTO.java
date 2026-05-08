package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record RegisterB2cProfileDTO(
        String email,
        String name,
        String surname,
        String password
) {
}
