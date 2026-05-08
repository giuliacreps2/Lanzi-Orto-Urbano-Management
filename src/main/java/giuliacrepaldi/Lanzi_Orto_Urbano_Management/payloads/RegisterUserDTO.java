package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record RegisterUserDTO(
        String name,
        String surname,
        String phoneNumber,
        String email,
        String password
) {
}
