package dao;

import java.util.List;


import model.AsignacionVehiculoGarage;
import model.Garage;


public interface AsignacionVehiculoGarageDAO{
    void guardar(AsignacionVehiculoGarage asignacion);
    
    void modificar(AsignacionVehiculoGarage asignacion);
    
    void eliminar(String matricula, int numeroGarage);
    
    AsignacionVehiculoGarage buscarPorGarage(Garage garage);
    
    List<AsignacionVehiculoGarage> listarTodas();
    
}
