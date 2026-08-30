package dao;

import model.Socio;

public interface SocioDAO extends GenericDAO<Socio, Integer> {

    Socio buscarPorDni(String dni);
}
