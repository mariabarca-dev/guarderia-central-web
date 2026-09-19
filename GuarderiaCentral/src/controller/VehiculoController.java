package controller;

import dto.VehiculoDTO;
import exceptions.ErrorNegocio;
import exceptions.MatriculaDuplicadaException;
import service.VehiculoService;
import service.AsignacionVehiculoGarageService;
import model.Usuario;
import model.Rol;
import model.TipoVehiculo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controlador de vehículos encargado exclusivamente de las validaciones de sintaxis,
 * formato (expresiones regulares) y delegación hacia la capa de servicio.
 */
public class VehiculoController implements Controlador {

    private final VehiculoService vehiculoService;
    private final AsignacionVehiculoGarageService asignacionVehiculoGarageService;

    // Patrón de sintaxis y formato para matrículas (ej. alfanumérico de 6 a 10 caracteres con guiones opcionales)
    private static final Pattern PATTERN_MATRICULA = Pattern.compile("^[A-Z0-9\\-]{6,10}$");
    private static final Pattern PATTERN_NOMBRE_VEHICULO = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]{2,50}$");
    private static final Pattern PATTERN_ID = Pattern.compile("^[0-9]+$");

    public VehiculoController() {
        this.vehiculoService = new VehiculoService();
        this.asignacionVehiculoGarageService = new AsignacionVehiculoGarageService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        throw new UnsupportedOperationException("El controlador de vehículos no soporta operaciones de inicio de sesión.");
    }

    public List<VehiculoDTO> listarTodosLosVehiculos(Usuario usuarioSesion) {
        validarEmpleadoOAdmin(usuarioSesion);
        return vehiculoService.listarTodos();
    }

    public VehiculoDTO buscarVehiculoPorId(Usuario usuarioSesion, int id) {
        validarEmpleadoOAdmin(usuarioSesion);

        if (id <= 0 || !PATTERN_ID.matcher(String.valueOf(id)).matches()) {
            throw new IllegalArgumentException("Error de formato: El ID del vehículo no es válido.");
        }

        try {
            return vehiculoService.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public void registrarVehiculo(Usuario usuarioSesion, VehiculoDTO dto) throws ErrorNegocio, MatriculaDuplicadaException {
        validarAdministrador(usuarioSesion);

        // 1. Validaciones de Sintaxis y Formato (Controller)
        if (dto == null) {
            throw new ErrorNegocio("El DTO del vehículo no puede ser nulo.");
        }
        if (dto.getMatricula() == null || !PATTERN_MATRICULA.matcher(dto.getMatricula().trim().toUpperCase()).matches()) {
            throw new ErrorNegocio("Formato de matrícula inválido (debe ser alfanumérico de 6 a 10 caracteres).");
        }
        if (dto.getNombre() != null && !dto.getNombre().trim().isEmpty() && !PATTERN_NOMBRE_VEHICULO.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El nombre del vehículo contiene caracteres no permitidos (2 a 50 caracteres alfanuméricos).");
        }
        if (dto.getSocioId() <= 0) {
            throw new ErrorNegocio("El ID del socio propietario debe ser un número positivo.");
        }
        if (dto.getEmpleadoId() < 0) {
            throw new ErrorNegocio("El ID del empleado responsable no puede ser negativo.");
        }
        if (dto.getTipo() == null) {
            throw new ErrorNegocio("El tipo de vehículo es obligatorio.");
        }
        if (dto.getProfundidad() <= 0) {
            throw new ErrorNegocio("La profundidad del vehículo debe ser un valor positivo.");
        }
        if (dto.getAncho() <= 0) {
            throw new ErrorNegocio("El ancho del vehículo debe ser un valor positivo.");
        }

        // 2. Delegación directa al servicio para reglas de negocio
        vehiculoService.registrarVehiculo(dto);
    }

    public void modificarVehiculo(Usuario usuarioSesion, VehiculoDTO dto) throws ErrorNegocio, MatriculaDuplicadaException {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El DTO del vehículo no puede ser nulo.");
        }
        if (dto.getId() <= 0) {
            throw new ErrorNegocio("El ID del vehículo no es válido para la modificación.");
        }
        if (dto.getMatricula() == null || !PATTERN_MATRICULA.matcher(dto.getMatricula().trim().toUpperCase()).matches()) {
            throw new ErrorNegocio("El formato de la matrícula es incorrecto.");
        }
        if (dto.getSocioId() <= 0) {
            throw new ErrorNegocio("El ID del socio propietario debe ser un número positivo.");
        }
        if (dto.getEmpleadoId() < 0) {
            throw new ErrorNegocio("El ID del empleado responsable no puede ser negativo.");
        }
        if (dto.getTipo() == null) {
            throw new ErrorNegocio("El tipo de vehículo es obligatorio.");
        }
        if (dto.getProfundidad() <= 0 || dto.getAncho() <= 0) {
            throw new ErrorNegocio("Las dimensiones de profundidad y ancho deben ser valores positivos.");
        }

        // Delegación directa al servicio para persistencia y reglas de negocio
        vehiculoService.actualizarVehiculo(dto);
    }

    public void eliminarVehiculo(Usuario usuarioSesion, int id) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (id <= 0 || !PATTERN_ID.matcher(String.valueOf(id)).matches()) {
            throw new ErrorNegocio("El ID ingresado debe ser un número positivo.");
        }

        try {
            VehiculoDTO v = vehiculoService.buscarPorId(id);
            if (v != null) {
                vehiculoService.eliminarVehiculo(v.getMatricula());
            } else {
                throw new ErrorNegocio("No se encontró el vehículo con ID: " + id);
            }
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al eliminar el vehículo: " + e.getMessage());
        }
    }

    public List<VehiculoDTO> listarVehiculosPorZona(Usuario usuarioSesion, int zonaId) {
        validarEmpleadoOAdmin(usuarioSesion);

        if (zonaId <= 0 || !PATTERN_ID.matcher(String.valueOf(zonaId)).matches()) {
            throw new IllegalArgumentException("Error de formato: El ID de la zona no es válido.");
        }

        List<VehiculoDTO> todosLosVehiculos = vehiculoService.listarTodos();
        List<VehiculoDTO> resultado = new ArrayList<>();

        for (VehiculoDTO v : todosLosVehiculos) {
            var asignacion = asignacionVehiculoGarageService.buscarPorVehiculo(v.getId());

            if (asignacion != null && asignacion.getGarage().getZona().getId() == zonaId) {
                resultado.add(v);
            }
        }
        return resultado;
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
}