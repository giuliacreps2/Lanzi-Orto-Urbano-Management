package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record RegisterAdminProfileDTO(
        String name,
        String surname,
        String email,
        String password
) {
}
