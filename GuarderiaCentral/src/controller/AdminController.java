package controller;

import dto.*;
import exceptions.ErrorNegocio;
import model.Rol;
import model.Usuario;
import service.AdministradorService;
import service.UsuarioService;

import java.util.List;
import java.util.regex.Pattern;

public class AdminController {

    private final AdministradorService administradorService;
    private final SocioController socioController;
    private final EmpleadoController empleadoController;
    private final UsuarioService usuarioService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");

    public AdminController() {
        this.administradorService = new AdministradorService();
        this.socioController = new SocioController();
        this.empleadoController = new EmpleadoController();
        this.usuarioService = new UsuarioService();
    }

    /**
     * Registra un nuevo Administrador recibiendo el DTO desde la vista.
     */
    public void registrarAdministrador(Usuario usuarioSesion, AdministradorDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El administrador a registrar no puede ser nulo.");
        }

        // Validaciones de sintaxis y formato sobre los atributos del DTO
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

        // Asegurar que el rol sea el correcto por seguridad
        dto.setRol(Rol.ADMINISTRADOR);

        // Delegación al servicio
        administradorService.registrarAdministrador(dto);
    }

    /**
     * Polimorfismo de alta de usuario: Registra un DTO delegando en los controladores correspondientes.
     */
    public void registrarUsuario(Usuario usuarioSesion, UsuarioDTO usuario) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (usuario == null) {
            throw new ErrorNegocio("El usuario a registrar no puede ser nulo.");
        }

        if (usuario instanceof SocioDTO socioDTO) {
            socioController.registrarSocio(usuarioSesion, socioDTO); // Delegación a SocioController
        } else if (usuario instanceof EmpleadoDTO empleadoDTO) {
            empleadoController.registrarEmpleado(usuarioSesion, empleadoDTO); // Delegación a EmpleadoController
        } else if (usuario instanceof AdministradorDTO administradorDTO) {
            registrarAdministrador(usuarioSesion, administradorDTO); // Reutiliza las validaciones de admin
        } else {
            throw new ErrorNegocio("Tipo de usuario no soportado: " + usuario.getClass().getSimpleName());
        }
    }

    /**
     * Lista todos los usuarios del sistema sin importar su tipo.
     */
    public List<UsuarioDTO> listarTodosLosUsuarios(Usuario usuarioSesion) {
        validarAdministrador(usuarioSesion);
        return usuarioService.listarTodos();
    }

    /**
     * Modifica los datos generales de un usuario.
     */
    public void modificarUsuario(Usuario usuarioSesion, UsuarioDTO u) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (u == null || u.getId() <= 0) {
            throw new ErrorNegocio("El usuario provisto para modificación no es válido.");
        }
        usuarioService.actualizarUsuario(u);
    }

    /**
     * Elimina un usuario del sistema por su ID.
     */
    public void eliminarUsuario(Usuario usuarioSesion, int id) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (id <= 0) {
            throw new ErrorNegocio("El ID de usuario a eliminar debe ser un entero positivo.");
        }
        usuarioService.eliminarUsuario(id);
    }

    // --- Métodos Privados de Validación de Sesión y Seguridad ---

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de ADMINISTRADOR.");
        }
    }
}