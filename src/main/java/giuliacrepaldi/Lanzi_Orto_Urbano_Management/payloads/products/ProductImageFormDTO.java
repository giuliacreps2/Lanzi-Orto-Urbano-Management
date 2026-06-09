package giuliacrepaldi.Lanzi_Orto_Urbano_Management.payloads.products;

public record ProductImageFormDTO(
        String urlImage,
        String altText,
        Integer sortOrder,
        boolean isPrimary,
        String cloudinaryPublicId
) {
}
