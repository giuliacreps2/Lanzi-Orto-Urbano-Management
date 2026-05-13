package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private String locality;
    @Column(nullable = false)
    private Integer postalCode;

    @ManyToOne
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;

    public Address(String street, Integer number, String locality, Integer postalCode, Municipality municipality) {
        this.street = street;
        this.number = number;
        this.locality = locality;
        this.postalCode = postalCode;
        this.municipality = municipality;
    }
}
