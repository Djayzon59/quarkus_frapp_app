package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Client", schema = "dbo", catalog = "filrougeDTB")
public class ClientEntity {


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="denominationSociale")
    private String denominationSociale;
    @Column(name="numeroRue")
    private String numRue;
    @Column(name="libelleRue")
    private String libelleRue;
    @Column(name="complementAdresse")
    private String complementAdresse;
    @Column(name="codePostal")
    private String codePostal;
    @Column(name="siren")
    private String siren;
    @Column(name="numTel")
    private String numTel;

    @OneToOne
    @JoinColumn(name = "id_user")
    private UtilisateurEntity utilisateurEntity;
    @OneToMany(mappedBy = "clientEntity")
    private List<EmployeEntity> employeEntities;

    public ClientEntity(String denominationSociale, String numRue, String libelleRue, String complementAdresse, String codePostal, String siren, String numTel) {
        this.denominationSociale = denominationSociale;
        this.numRue = numRue;
        this.libelleRue = libelleRue;
        this.complementAdresse = complementAdresse;
        this.codePostal = codePostal;
        this.siren = siren;
        this.numTel = numTel;
    }
}



