package giuliacrepaldi.Lanzi_Orto_Urbano_Management.utilities;

import giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.B2bProfile;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class B2bProfileSpecs {

    //Id
    public static Specification<B2bProfile> searchById(UUID b2bProfileId) {
        return (root, query, builder) ->
                b2bProfileId == null ? null : builder.equal(root.get("b2bProfileId"), b2bProfileId);
    }

    //Nome contatto
    public static Specification<B2bProfile> hasContactName(String contactName) {
        return (root, query, builder) -> {
            if (contactName == null || contactName.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("contactName")),
                    "%" + contactName.toLowerCase() + "%");
        };
    }

    //Cognome contatto
    public static Specification<B2bProfile> hasContactSurname(String contactSurname) {
        return (root, query, builder) -> {
            if (contactSurname == null || contactSurname.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("contactSurname")),
                    "%" + contactSurname.toLowerCase() + "%");
        };
    }

    //Nome compagnia
    public static Specification<B2bProfile> hasCompanyName(String companyName) {
        return (root, query, builder) -> {
            if (companyName == null || companyName.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("companyName")),
                    "%" + companyName.toLowerCase() + "%");
        };
    }

    //Mail contatto
    public static Specification<B2bProfile> hasContactEmail(String contactEmail) {
        return (root, query, builder) -> {
            if (contactEmail == null || contactEmail.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(builder.lower(root.get("contactEmail")), contactEmail.toLowerCase());
        };
    }

    //P.iva
    public static Specification<B2bProfile> vatNumberStartsWith(String vatNumber) {
        return (root, query, builder) -> {
            if (vatNumber == null || vatNumber.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(root.get("vatNumber"), vatNumber + "%");
        };
    }

    //Fiscal Code
    public static Specification<B2bProfile> fiscalCodeStartsWith(String fiscalCode) {
        return (root, query, builder) -> {
            if (fiscalCode == null || fiscalCode.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(root.get("fiscalCode"), fiscalCode + "%");
        };
    }

    //Numero di telefono
    public static Specification<B2bProfile> contactPhoneStartsWith(String contactPhone) {
        return (root, query, builder) -> {
            if (contactPhone == null || contactPhone.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(root.get("contactPhone"), contactPhone + "%");
        };
    }

    //Provincia
    public static Specification<B2bProfile> livesInProvinces(String provinceName) {
        return (root, query, builder) -> {
            if (provinceName == null || provinceName.isEmpty()) {
                return builder.conjunction();
            }
            return builder.equal(
                    root.join("legalAddress")
                            .join("municipality")
                            .join("province")
                            .get("provinceName"),
                    provinceName
            );
        };
    }
}
