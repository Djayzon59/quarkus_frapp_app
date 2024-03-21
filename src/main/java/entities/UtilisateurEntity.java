package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Utilisateur", schema = "dbo", catalog = "filrougeDTB")
public class UtilisateurEntity {

    @Id
    @Column(name = "mail_utilisateur", nullable = false)
    private String mail_utilisateur;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name ="id_role")
    private int idRole;

}
