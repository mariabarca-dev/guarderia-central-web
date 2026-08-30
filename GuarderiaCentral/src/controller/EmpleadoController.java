package controller;

import service.AsignacionEmpleadoZonaService;
import service.VehiculoService; // Importación necesaria
import model.Usuario;
import model.Rol;
import dto.ZonaDTO; // Importación necesaria
import dto.VehiculoDTO; // Importación necesaria
import mapper.AsignacionEmpleadoZonaMapper;
import java.util.List;
import java.util.stream.Collectors;

//prueba

public class EmpleadoController {
    
    private AsignacionEmpleadoZonaService asignacionService;
    private VehiculoService vehiculoService; // Servicio para gestionar vehículos

    public EmpleadoController(Usuario usuario) {
        if (usuario == null || (usuario.getRol() != Rol.EMPLEADO && usuario.getRol() != Rol.ADMINISTRADOR)) {
            throw new SecurityException("Acceso denegado: No tiene permisos para acceder a la gestión de empleados.");
        }
        // Inicializamos los servicios necesarios
        this.asignacionService = new AsignacionEmpleadoZonaService();
        this.vehiculoService = new VehiculoService(); 
    }

    /**
     * Lista las zonas asignadas a un empleado específico.
     * @param empleadoId El ID del empleado logueado (int).
     * @return Una lista de ZonaDTO listos para mostrarse en la vista.
     */
    public List<ZonaDTO> listarZonasAsignadas(int empleadoId) {
        // Obtenemos todas las asignaciones, filtramos por el ID del empleado,
        // mapeamos la asignacion a su DTO y extraemos la ZonaDTO correspondiente.
        return asignacionService.listarTodas().stream()
                .filter(asig -> asig.getEmpleado() != null && asig.getEmpleado().getId() == empleadoId)
                .map(AsignacionEmpleadoZonaMapper::toDto)
                .map(dto -> dto.getZona()) // Extraemos el objeto ZonaDTO del DTO de asignación
                .collect(Collectors.toList());
    }

    /**
     * Lista los vehículos bajo la responsabilidad de un empleado específico.
     * @param empleadoId El ID del empleado logueado (int).
     * @return Una lista de VehiculoDTO listos para mostrarse en la vista.
     */
    public List<VehiculoDTO> listarVehiculosBajoResponsabilidad(int empleadoId) {
        // Delegamos la búsqueda al servicio de vehículos utilizando el nuevo
        // campo 'empleadoId' en el modelo Vehiculo (el cual se mapea al DTO).
        return vehiculoService.listarVehiculosPorResponsable(empleadoId);
    }
}