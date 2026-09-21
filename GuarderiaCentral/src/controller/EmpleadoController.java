package controller;

import dto.EmpleadoDTO;
import exceptions.ErrorNegocio;
import service.AsignacionEmpleadoZonaService;
import service.EmpleadoService;
import service.VehiculoService;
import model.Usuario;
import model.Rol;
import model.Empleado;
import dto.ZonaDTO;
import dto.VehiculoDTO;
import mapper.AsignacionEmpleadoZonaMapper;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmpleadoController implements Controlador {

    private final AsignacionEmpleadoZonaService asignacionService;
    private final VehiculoService vehiculoService;
    private final EmpleadoService empleadoService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");
    private static final Pattern PATTERN_CODIGO = Pattern.compile("^[a-zA-Z0-9\\-_]{3,20}$");
    private static final Pattern PATTERN_ESPECIALIDAD = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\.\\-\\_]{3,100}$");
    private static final Pattern PATTERN_ID = Pattern.compile("^[0-9]+$");

    public EmpleadoController() {
        this.asignacionService = new AsignacionEmpleadoZonaService();
        this.vehiculoService = new VehiculoService();
        this.empleadoService = new EmpleadoService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        throw new UnsupportedOperationException("El controlador de empleados no soporta operaciones de inicio de sesión.");
    }

    /**
     * Lista todos los empleados registrados en el sistema.
     * Acceso: Solo ADMINISTRADOR.
     */
    public List<EmpleadoDTO> listarTodosLosEmpleados(Usuario usuarioSesion) {
        validarAdministrador(usuarioSesion);
        return empleadoService.listarTodos();
    }

    /**
     * Lista las zonas asignadas a un empleado específico.
     * Acceso: EMPLEADO (solo las suyas) o ADMINISTRADOR (cualquiera).
     */
    public List<ZonaDTO> listarZonasAsignadas(Usuario usuarioSesion, int empleadoId) {
        validarEmpleadoOAdmin(usuarioSesion);
        validarPermisoEmpleado(usuarioSesion, empleadoId);

        return asignacionService.listarTodas().stream()
                .filter(asig -> asig.getEmpleado() != null && asig.getEmpleado().getId() == empleadoId)
                .map(AsignacionEmpleadoZonaMapper::toDto)
                .map(dto -> dto.getZona())
                .collect(Collectors.toList());
    }

    /**
     * Lista los vehículos bajo la responsabilidad de un empleado específico.
     * Acceso: EMPLEADO (solo los suyos) o ADMINISTRADOR (cualquiera).
     */
    public List<VehiculoDTO> listarVehiculosBajoResponsabilidad(Usuario usuarioSesion, int empleadoId) {
        validarEmpleadoOAdmin(usuarioSesion);
        validarPermisoEmpleado(usuarioSesion, empleadoId);

        return vehiculoService.listarVehiculosPorResponsable(empleadoId);
    }

    /**
     * Registra un nuevo empleado validando la sintaxis de los campos de su DTO.
     * Acceso: Solo ADMINISTRADOR.
     */
    public void registrarEmpleado(Usuario usuarioSesion, EmpleadoDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El DTO del empleado no puede ser nulo.");
        }

        // Validaciones de sintaxis y formato (Controlador)
        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El nombre debe tener entre 2 y 100 caracteres alfabéticos.");
        }

        if (dto.getApellido() == null || !PATTERN_NOMBRE.matcher(dto.getApellido().trim()).matches()) {
            throw new ErrorNegocio("El apellido debe tener entre 2 y 100 caracteres alfabéticos.");
        }

        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty() || dto.getDireccion().length() > 200) {
            throw new ErrorNegocio("La dirección es obligatoria y no puede superar los 200 caracteres.");
        }

        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono().trim()).matches()) {
            throw new ErrorNegocio("El teléfono no es válido (7 a 20 caracteres permitiendo números, +, espacios y guiones).");
        }

        if (dto.getNombreUsuario() == null || !PATTERN_USUARIO.matcher(dto.getNombreUsuario().trim()).matches()) {
            throw new ErrorNegocio("El nombre de usuario debe tener entre 4 y 20 caracteres.");
        }

        if (dto.getClave() == null || !PATTERN_CLAVE.matcher(dto.getClave()).matches()) {
            throw new ErrorNegocio("La clave debe contener al menos 8 caracteres, mayúscula, minúscula, número y carácter especial.");
        }

        if (dto.getCodigo() == null || !PATTERN_CODIGO.matcher(dto.getCodigo().trim()).matches()) {
            throw new ErrorNegocio("El código del empleado no es válido (debe tener entre 3 y 20 caracteres alfanuméricos).");
        }

        if (dto.getEspecialidad() == null || !PATTERN_ESPECIALIDAD.matcher(dto.getEspecialidad().trim()).matches()) {
            throw new ErrorNegocio("La especialidad no es válida (debe tener entre 3 y 100 caracteres).");
        }

        // Asegurar que el rol sea el correcto por seguridad
        dto.setRol(Rol.EMPLEADO);

        // Delegación al servicio
        empleadoService.registrarEmpleado(dto);
    }

    // --- Métodos Privados de Validación de Sesión y Seguridad ---

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarEmpleadoOAdmin(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.EMPLEADO && usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de EMPLEADO o ADMINISTRADOR.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de ADMINISTRADOR.");
        }
    }

    private void validarPermisoEmpleado(Usuario usuarioSesion, int empleadoId) {
        if (usuarioSesion.getRol() == Rol.EMPLEADO && usuarioSesion instanceof Empleado empleado) {
            if (empleado.getId() != empleadoId) {
                throw new SecurityException("Acceso denegado: No tiene permisos para consultar información de otro empleado.");
            }
        }
    }
}
