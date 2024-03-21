package repo;

import entities.ClientEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import javax.swing.*;
import java.util.List;

@RequestScoped
public class ClientRepo implements PanacheRepositoryBase<ClientEntity,Integer> {

    public ClientEntity findByIdUser(String id_user) {
       return  find("id_user", id_user).firstResult();
    }
}
