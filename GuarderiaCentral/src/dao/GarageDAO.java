package dao;

import java.util.List;
import model.Garage;

public interface GarageDAO extends GenericDAO{
    
    void guardar(Garage garage);
    void actualizar(Garage garage);
    void eliminar(int numeroGarage);
    
    Garage buscarPorNumero(int numeroGarage);
    List<Garage> listarTodos();
    Garage buscarPorId(int id);
    List<Garage> listarPorSocio(int socioId);
    //se deberá agregar un método estáVendido para validar la fecha de compra (en caso que esté vendido)
}
