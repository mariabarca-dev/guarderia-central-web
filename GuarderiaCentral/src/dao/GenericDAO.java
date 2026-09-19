package dao;

import java.util.List;

public interface GenericDAO<T, ID> {

    void guardar(T objeto);

    void actualizar(T objeto);

    void eliminar(ID id);

    T buscarPorId(ID id);

    List<T> listarTodos();

}
