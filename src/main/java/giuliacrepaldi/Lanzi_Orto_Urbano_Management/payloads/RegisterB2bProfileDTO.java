package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads;

public record RegisterB2bProfileDTO(
        String contactName,
        String contactSurname,
        String contactEmail,
        String contactPhone,
        String vatNumber,
        String fiscalCode,
        String companyName
) {
}
