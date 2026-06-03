package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.orders;

public record AdminOrderCustomerDTO(
        String customerType,
        String name,
        String email,
        String phone,
        String companyName,
        String address
) {
}
