package dao;

import java.util.List;
import model.Vehiculo;

public interface VehiculoDAO{
    void guardar(Vehiculo vehiculo);
    void actualizar(Vehiculo vehiculo);
    void eliminar(String matricula);
    Vehiculo buscarPorMatricula(String matricula);
    List<Vehiculo> listarTodos();
    Vehiculo buscarPorId(int id);
    
    /**
     * --- CORRECCIÓN: Añadido método para el menú empleado ---
     * Busca todos los vehículos asignados a un empleado específico.
     * @param empleadoId El ID del empleado responsable.
     * @return Una lista de Vehiculo.
     */
    List<Vehiculo> buscarPorEmpleado(int empleadoId);
}