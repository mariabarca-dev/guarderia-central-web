package controller;

import exceptions.*;
import service.*;
import dto.*;
import exceptions.RegistroNoEncontradoException;
import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class SocioController implements Controlador {

    private final SocioService socioService;
    private final VehiculoService vehiculoService;
    private final PropiedadGarageService propiedadGarageService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");
    private static final Pattern PATTERN_DNI = Pattern.compile("^\\d{7,8}$");
    private static final Pattern PATTERN_ID = Pattern.compile("^[0-9]+$");

    public SocioController() {
        this.socioService = new SocioService();
        this.vehiculoService = new VehiculoService();
        this.propiedadGarageService = new PropiedadGarageService();
    }

    public List<SocioDTO> listarTodosLosSocios(Usuario usuarioSesion) {
        validarAdministrador(usuarioSesion);
        return socioService.listarTodos();
    }

    public SocioDTO buscarSocioPorId(Usuario usuarioSesion, int id) {
        validarSocioOAdmin(usuarioSesion);

        if (id <= 0 || !PATTERN_ID.matcher(String.valueOf(id)).matches()) {
            throw new IllegalArgumentException("Error de formato: El ID del socio no es válido.");
        }

        try {
            return socioService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public SocioDTO buscarSocioPorDni(Usuario usuarioSesion, String dni) {
        validarSocioOAdmin(usuarioSesion);

        if (dni == null || !PATTERN_DNI.matcher(dni.trim()).matches()) {
            throw new IllegalArgumentException("Error de formato: El DNI proporcionado no es válido.");
        }

        try {
            return socioService.buscarPorDni(dni.trim());
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public void registrarSocio(Usuario usuarioSesion, SocioDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El socio no puede ser nulo.");
        }

        // Validación de DNI
        if (dto.getDni() == null || !PATTERN_DNI.matcher(dto.getDni()).matches()) {
            throw new ErrorNegocio("El formato del DNI no es válido (debe tener entre 7 y 8 dígitos numéricos).");
        }

        // Validación de Nombre
        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El nombre es obligatorio, debe tener entre 2 y 100 caracteres y contener solo letras y espacios.");
        }

        // Validación de Apellido
        if (dto.getApellido() == null || !PATTERN_NOMBRE.matcher(dto.getApellido().trim()).matches()) {
            throw new ErrorNegocio("El apellido es obligatorio, debe tener entre 2 y 100 caracteres y contener solo letras y espacios.");
        }

        // Validación de Dirección
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty() || dto.getDireccion().length() > 200) {
            throw new ErrorNegocio("La dirección es obligatoria y no puede superar los 200 caracteres.");
        }

        // Validación de Teléfono
        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono()).matches()) {
            throw new ErrorNegocio("El formato del teléfono no es válido (7 a 20 caracteres permitiendo +, espacios y guiones).");
        }

        // Validación de Nombre de Usuario
        if (dto.getNombreUsuario() == null || !PATTERN_USUARIO.matcher(dto.getNombreUsuario()).matches()) {
            throw new ErrorNegocio("El nombre de usuario debe tener entre 4 y 20 caracteres (letras, números, puntos o guiones bajos).");
        }

        // Validación de Clave
        if (dto.getClave() == null || !PATTERN_CLAVE.matcher(dto.getClave()).matches()) {
            throw new ErrorNegocio("La clave no cumple con los requisitos de seguridad (mínimo 8 caracteres, incluir mayúscula, minúscula, número y carácter especial).");
        }

        // Validación de Rol
        if (dto.getRol() == null) {
            throw new ErrorNegocio("El rol del usuario es obligatorio.");
        }

        // Validación de Fecha de Ingreso
        if (dto.getFechaIngreso() == null) {
            throw new ErrorNegocio("La fecha de ingreso es obligatoria.");
        }
        if (dto.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ErrorNegocio("La fecha de ingreso no puede estar en el futuro.");
        }

        socioService.registrarSocio(dto);
    }

    public void modificarSocio(Usuario usuarioSesion, SocioDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null || dto.getId() <= 0) {
            throw new ErrorNegocio("El ID del socio no es válido para la modificación.");
        }

        if (dto.getDni() == null || !PATTERN_DNI.matcher(dto.getDni()).matches()) {
            throw new ErrorNegocio("El formato del DNI es incorrecto.");
        }

        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El formato del nombre no es válido.");
        }

        if (dto.getApellido() == null || !PATTERN_NOMBRE.matcher(dto.getApellido().trim()).matches()) {
            throw new ErrorNegocio("El formato del apellido no es válido.");
        }

        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono()).matches()) {
            throw new ErrorNegocio("El formato del teléfono no es válido.");
        }

        if (dto.getFechaIngreso() != null && dto.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ErrorNegocio("La fecha de ingreso no puede estar en el futuro.");
        }

        socioService.actualizarSocio(dto);
    }

    public void eliminarSocio(Usuario usuarioSesion, int id) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (id <= 0 || !PATTERN_ID.matcher(String.valueOf(id)).matches()) {
            throw new ErrorNegocio("El ID ingresado debe ser un número positivo.");
        }
        socioService.eliminarSocio(id);
    }

    public List<VehiculoDTO> consultarMisVehiculos(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);
        return vehiculoService.listarPorSocio(socioId);
    }

    public void consultarMiGarage(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);

        String reporte = propiedadGarageService.obtenerEstadoGarageSocio(socioId);
        System.out.println("--- Estado de mi Garage Propio ---");
        System.out.println(reporte);
        System.out.println("----------------------------------");
    }

    public List<VehiculoDTO> listarVehiculosPorSocio(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);
        return vehiculoService.listarPorSocio(socioId);
    }

    public List<GarageDTO> listarGarajesPorSocio(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);
        return propiedadGarageService.listarPorSocio(socioId);
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        // La autenticación centralizada se maneja en LoginController
    }

    // --- Métodos Privados de Validación de Sesión y Seguridad ---

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarSocioOAdmin(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.SOCIO && usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de SOCIO o ADMINISTRADOR.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de ADMINISTRADOR.");
        }
    }

    private void validarPermisoSocio(Usuario usuarioSesion, int socioId) {
        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (socio.getId() != socioId) {
                throw new SecurityException("Acceso denegado: No tiene permisos para consultar información de otro socio.");
            }
        }
    }
}