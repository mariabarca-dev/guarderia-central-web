package controller;

import dto.EmpleadoDTO;
import exceptions.ErrorNegocio;
import service.EmpleadoService;
import model.Usuario;
import model.Rol;
import model.Empleado;
import dto.ZonaDTO;
import dto.VehiculoDTO;

import java.util.List;
import java.util.regex.Pattern;

public class EmpleadoController implements Controlador {

    private final EmpleadoService empleadoService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");
    private static final Pattern PATTERN_CODIGO = Pattern.compile("^[a-zA-Z0-9\\-_]{3,20}$");
    private static final Pattern PATTERN_ESPECIALIDAD = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s\\.\\-\\_]{3,100}$");

    public EmpleadoController() {
        this.empleadoService = new EmpleadoService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        throw new UnsupportedOperationException("El controlador de empleados no soporta operaciones de inicio de sesión.");
    }

    public List<EmpleadoDTO> listarTodosLosEmpleados(Usuario usuarioSesion) {
        validarAdministrador(usuarioSesion);
        return empleadoService.listarTodos();
    }

    public List<ZonaDTO> listarZonasAsignadas(Usuario usuarioSesion, int empleadoId) {
        validarEmpleadoOAdmin(usuarioSesion);
        validarPermisoEmpleado(usuarioSesion, empleadoId);
        return empleadoService.listarZonasAsignadas(empleadoId);
    }

    public List<VehiculoDTO> listarVehiculosBajoResponsabilidad(Usuario usuarioSesion, int empleadoId) {
        validarEmpleadoOAdmin(usuarioSesion);
        validarPermisoEmpleado(usuarioSesion, empleadoId);
        return empleadoService.listarVehiculosBajoResponsabilidad(empleadoId);
    }

    public void registrarEmpleado(Usuario usuarioSesion, EmpleadoDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El DTO del empleado no puede ser nulo.");
        }

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
            throw new ErrorNegocio("El teléfono no es válido.");
        }
        if (dto.getNombreUsuario() == null || !PATTERN_USUARIO.matcher(dto.getNombreUsuario().trim()).matches()) {
            throw new ErrorNegocio("El nombre de usuario debe tener entre 4 y 20 caracteres.");
        }
        if (dto.getClave() == null || !PATTERN_CLAVE.matcher(dto.getClave()).matches()) {
            throw new ErrorNegocio("La clave no cumple con los requisitos de seguridad.");
        }
        if (dto.getCodigo() == null || !PATTERN_CODIGO.matcher(dto.getCodigo().trim()).matches()) {
            throw new ErrorNegocio("El código del empleado no es válido.");
        }
        if (dto.getEspecialidad() == null || !PATTERN_ESPECIALIDAD.matcher(dto.getEspecialidad().trim()).matches()) {
            throw new ErrorNegocio("La especialidad no es válida.");
        }

        dto.setRol(Rol.EMPLEADO);
        empleadoService.registrarEmpleado(dto);
    }

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