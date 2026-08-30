package dao;

import model.Empleado;

public interface EmpleadoDAO extends GenericDAO<Empleado, Integer>{
    Empleado buscarPorCodigo(String codigo);
}
