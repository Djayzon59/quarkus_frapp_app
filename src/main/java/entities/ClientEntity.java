package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Client", schema = "dbo", catalog = "filrougeDTB")
public class ClientEntity {


    @Id
    @Column(name = "id_client", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="denominationSociale")
    private String denominationSociale;
    @Column(name="numeroRueClient")
    private String numRue;
    @Column(name="libelleRueClient")
    private String libelleRue;
    @Column(name="complementAdresseClient")
    private String complementAdresse;
    @Column(name="codePostalClient")
    private String codePostal;
    @Column(name="siren")
    private String siren;
    @Column(name="numTelClient")
    private String numTel;
    @Column(name="id_user")
    private String idUser;

    @ManyToOne
    @JoinColumn(name = "id_ville")
    private VilleEntity villeEntity;



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



