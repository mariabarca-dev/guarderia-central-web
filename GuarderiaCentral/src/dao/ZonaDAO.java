package dao;

import java.util.List;
import model.Zona;

public interface ZonaDAO {
    void guardar(Zona zona);
    void actualizar(Zona zona);
    void eliminar(String letra);
    void eliminar(Integer id);
    Zona buscarPorLetra(String letra);
    List<Zona> listarTodos();
    Zona buscarPorId(Integer id);
}