package dao;

import java.util.List;
import model.PropiedadGarage;

public interface PropiedadGarageDAO {
    void guardar(PropiedadGarage propiedad);
    
    void eliminar(int socioId, int numeroGarage);
    
    void actualizar(PropiedadGarage propiedad);
    
    List<PropiedadGarage> listarTodas();
    
    PropiedadGarage buscarPorSocio(int socioID);
    
}
