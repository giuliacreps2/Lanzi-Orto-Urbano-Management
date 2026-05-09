package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record AdminRoleDTO(
        String name,
        String surname,
        String email,
        String password,
        String adminRoleName,
        String adminRoleSurname
) {
}
