package dao;

import java.util.List;
import model.Administrador;
//prueva conecion hector
public interface AdministradorDAO extends GenericDAO<Administrador, Integer> {

    boolean existeAdministrador(String username);

}

