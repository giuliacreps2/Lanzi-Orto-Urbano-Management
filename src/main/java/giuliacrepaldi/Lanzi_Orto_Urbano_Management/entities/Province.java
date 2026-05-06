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
@Table(name = "provinces")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID provinceId;

    @Column(nullable = false)
    private String provinceName;

    @Column(nullable = false)
    private String acronym;


    public Province(String provinceName, String acronym) {
        this.provinceName = provinceName;
        this.acronym = acronym;
    }
}
