package controller;

import dto.EmpleadoDTO;
import exceptions.ErrorNegocio;
import service.AsignacionEmpleadoZonaService;
import service.EmpleadoService;
import service.VehiculoService; // Importación necesaria
import model.Usuario;
import model.Rol;
import dto.ZonaDTO; // Importación necesaria
import dto.VehiculoDTO; // Importación necesaria
import mapper.AsignacionEmpleadoZonaMapper;
import java.util.List;
import java.util.stream.Collectors;

public class EmpleadoController {
    
    private AsignacionEmpleadoZonaService asignacionService;
    private VehiculoService vehiculoService; // Servicio para gestionar vehículos
    private EmpleadoService empleadoService;


    public EmpleadoController(Usuario usuario) {
        if (usuario == null || (usuario.getRol() != Rol.EMPLEADO && usuario.getRol() != Rol.ADMINISTRADOR)) {
            throw new SecurityException("Acceso denegado: No tiene permisos para acceder a la gestión de empleados.");
        }
        // Inicializamos los servicios necesarios
        this.asignacionService = new AsignacionEmpleadoZonaService();
        this.vehiculoService = new VehiculoService();
        this.empleadoService = new EmpleadoService();
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

    public String registrarEmpleado(String nombre, String direccion, String telefono,
                                    String nombreUsuario, String clave, String rolStr,
                                    String codigo, String especialidad) {

        // --- 1. VALIDACIONES DE SINTAXIS Y FORMATO (Controller) ---
        if (nombre == null || nombre.isBlank() || codigo == null || codigo.isBlank()) {
            return "Error de sintaxis: El nombre y el código no pueden estar vacíos.";
        }

        if (telefono != null && !telefono.matches("\\d+")) {
            return "Error de formato: El teléfono solo debe contener números.";
        }

        Rol rol;
        try {
            rol = Rol.valueOf(rolStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error de formato: El rol especificado no es válido.";
        }

        // --- 2. ARMADO DEL DTO Y LLAMADA AL SERVICE ---
        try {
            EmpleadoDTO dto = new EmpleadoDTO();
            dto.setNombre(nombre.trim());
            dto.setDireccion(direccion);
            dto.setTelefono(telefono);
            dto.setNombreUsuario(nombreUsuario);
            dto.setClave(clave);
            dto.setRol(rol);
            dto.setCodigo(codigo.trim());
            dto.setEspecialidad(especialidad);

            // Llamada al servicio (las validaciones de negocio ocurrirán aquí)
            empleadoService.registrarEmpleado(dto);
            return "Empleado registrado exitosamente.";

        } catch (ErrorNegocio e) {
            // Atrapamos la excepción de negocio lanzada por el Service
            return e.getMessage();
        }
    }
}