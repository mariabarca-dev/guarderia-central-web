package dao;

import java.util.List;
import model.Administrador;

public interface AdministradorDAO extends GenericDAO<Administrador, Integer> {

    boolean existeAdministrador(String username);

}

