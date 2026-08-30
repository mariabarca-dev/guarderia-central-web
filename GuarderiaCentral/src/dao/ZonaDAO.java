package dao;

import java.util.List;
import model.Zona;

public interface ZonaDAO{
    void guardar(Zona zona);
    void actualizar(Zona zona);
    void eliminar(String letra);
    Zona buscarPorLetra(String letra);
    List<Zona> listarTodos();
    Zona buscarPorId(int id);
}
