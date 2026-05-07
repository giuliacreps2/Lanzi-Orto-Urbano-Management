package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;


import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.TypeActivity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "b2b_profiles")
public class B2bProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID b2bProfileId;

    @Column(nullable = true, unique = true)
    private String vatNumber;
    @Column(nullable = true, unique = true)
    private String fiscalCode;
    @Column(nullable = false)
    private String contactPhone;
    @Column(nullable = false)
    private String contactName;
    @Column(nullable = false)
    private String contactSurname;
    @Column(nullable = false)
    private String contactEmail;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private Long loyaltyPoints = 0L;

    private LocalDateTime loyaltyLastActivity;
    private LocalDateTime vatVerifiedAt;
    private LocalDateTime fiscalCodeVerifiedAt;

    private String notes;
    private Boolean autoReorderEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeActivity typeActivity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusB2b statusB2b;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public B2bProfile(String vatNumber, String fiscalCode, String contactName, String contactSurname, String contactPhone, String contactEmail, String companyName, Long loyaltyPoints, TypeActivity typeActivity, StatusB2b statusB2b, User user) {
        this.vatNumber = vatNumber;
        this.fiscalCode = fiscalCode;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.companyName = companyName;
        this.loyaltyPoints = loyaltyPoints;
        this.typeActivity = typeActivity;
        this.statusB2b = statusB2b;
        this.user = user;
    }

    public B2bProfile(String vatNumber, String fiscalCode, String contactName, String contactSurname, String contactPhone, String contactEmail, String companyName,
                      Long loyaltyPoints, LocalDateTime loyaltyLastActivity, LocalDateTime vatVerifiedAt, LocalDateTime fiscalCodeVerifiedAt, String notes,
                      Boolean autoReorderEnabled, TypeActivity typeActivity, StatusB2b statusB2b, User user) {
        this.vatNumber = vatNumber;
        this.fiscalCode = fiscalCode;
        this.contactName = contactName;
        this.contactSurname = contactSurname;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.companyName = companyName;
        this.loyaltyPoints = loyaltyPoints;
        this.loyaltyLastActivity = loyaltyLastActivity;
        this.vatVerifiedAt = vatVerifiedAt;
        this.fiscalCodeVerifiedAt = fiscalCodeVerifiedAt;
        this.notes = notes;
        this.autoReorderEnabled = autoReorderEnabled;
        this.typeActivity = typeActivity;
        this.statusB2b = statusB2b;
        this.user = user;
    }

    
}
