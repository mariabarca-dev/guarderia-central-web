package dao;

import java.util.List;
import model.Vehiculo;

public interface VehiculoDAO extends  GenericDAO<Vehiculo , Integer>{


    void eliminar(String matricula);
    Vehiculo buscarPorMatricula(String matricula);


    /**
     * --- CORRECCIÓN: Añadido método para el menú empleado ---
     * Busca todos los vehículos asignados a un empleado específico.
     * @param empleadoId El ID del empleado responsable.
     * @return Una lista de Vehiculo.
     */
    List<Vehiculo> buscarPorEmpleado(int empleadoId);
}