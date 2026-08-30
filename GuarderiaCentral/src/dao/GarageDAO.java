package dao;

import java.util.List;
import model.Garage;

public interface GarageDAO {
    
    void guardar(Garage garage);
    void actualizar(Garage garage);
    void eliminar(int numeroGarage);
    
    Garage buscarPorNumero(int numeroGarage);
    List<Garage> listarTodos();
    Garage buscarPorId(int id);
    List<Garage> listarPorSocio(int socioId);
}
