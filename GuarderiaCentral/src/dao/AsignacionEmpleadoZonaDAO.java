package dao;

import java.util.List;


import model.AsignacionEmpleadoZona;


public interface AsignacionEmpleadoZonaDAO {
    void guardar(AsignacionEmpleadoZona asignacion);

    void modificar(AsignacionEmpleadoZona asignacion);

    void eliminar(String codigoEmpleado, int zonaId);

    List<AsignacionEmpleadoZona> listarTodas();
    List<AsignacionEmpleadoZona> buscarPorEmpleado(String codigoEmpleado);

    int contarVehiculosEnZona(int zonaId);
}