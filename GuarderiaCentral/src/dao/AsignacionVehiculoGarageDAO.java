package dao;

import java.util.List;

import model.Administrador;
import model.AsignacionVehiculoGarage;
import model.Garage;
import model.Vehiculo;

public interface AsignacionVehiculoGarageDAO extends GenericDAO<AsignacionVehiculoGarage, Integer> {
    void guardar(AsignacionVehiculoGarage asignacion);
    
    void modificar(AsignacionVehiculoGarage asignacion);
    
    void eliminar(String matricula, int numeroGarage);
    
    AsignacionVehiculoGarage buscarPorGarage(Garage garage);
    
    List<AsignacionVehiculoGarage> listarTodas();
    
}
