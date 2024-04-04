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
@Table(name = "Utilisateur", schema = "dbo", catalog = "filrougeDTB")
public class UtilisateurEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "mail_utilisateur", nullable = false)
    private String mail_utilisateur;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name ="id_role")
    private int idRole;

    @OneToOne(mappedBy = "utilisateurEntity")
    private EmployeEntity employeEntity;


}
