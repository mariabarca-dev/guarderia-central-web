package controller;

import dto.*;
import exceptions.ErrorNegocio;
import model.Rol;
import model.Usuario;
import service.AdministradorService;
import service.EmpleadoService;
import service.SocioService;
import service.UsuarioService;

import java.util.List;
import java.util.regex.Pattern;

public class AdminController {

    private final AdministradorService administradorService;
    private final SocioService socioService;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;

    // Patrones de validación de sintaxis y formato (opcional si se validan en el DTO/Vista)
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");

    public AdminController(Usuario usuario) {
        if (usuario == null || usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren privilegios de Administrador para esta sección.");
        }
        this.administradorService = new AdministradorService();
        this.socioService = new SocioService();
        this.empleadoService = new EmpleadoService();
        this.usuarioService = new UsuarioService();
    }

    /**
     * Registra un nuevo Administrador recibiendo el DTO desde la vista.
     */
    public void registrarAdministrador(AdministradorDTO dto) throws ErrorNegocio {
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
     * Polimorfismo de alta de usuario: Registra un DTO invocando al servicio correspondiente.
     */
    public void registrarUsuario(UsuarioDTO usuario) throws ErrorNegocio {
        if (usuario == null) {
            throw new ErrorNegocio("El usuario a registrar no puede ser nulo.");
        }

        if (usuario instanceof SocioDTO socioDTO) {
            socioService.registrarSocio(socioDTO);
        } else if (usuario instanceof EmpleadoDTO empleadoDTO) {
            empleadoService.registrarEmpleado(empleadoDTO);
        } else if (usuario instanceof AdministradorDTO administradorDTO) {
            registrarAdministrador(administradorDTO); // Reutiliza las validaciones de admin
        } else {
            throw new ErrorNegocio("Tipo de usuario no soportado: " + usuario.getClass().getSimpleName());
        }
    }

    /**
     * Lista todos los usuarios del sistema sin importar su tipo.
     */
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioService.listarTodos();
    }

    /**
     * Modifica los datos generales de un usuario.
     */
    public void modificarUsuario(UsuarioDTO u) throws ErrorNegocio {
        if (u == null || u.getId() <= 0) {
            throw new ErrorNegocio("El usuario provisto para modificación no es válido.");
        }
        usuarioService.actualizarUsuario(u);
    }

    /**
     * Elimina un usuario del sistema por su ID.
     */
    public void eliminarUsuario(int id) throws ErrorNegocio {
        if (id <= 0) {
            throw new ErrorNegocio("El ID de usuario a eliminar debe ser un entero positivo.");
        }
        usuarioService.eliminarUsuario(id);
    }
}