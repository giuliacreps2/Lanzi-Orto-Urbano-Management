package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table
public class Municipality {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID municipalityId;

    @Column(nullable = false)
    private String municipalityName;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;


    public Municipality(String municipalityName, Province province) {
        this.municipalityName = municipalityName;
        this.province = province;
    }


}
