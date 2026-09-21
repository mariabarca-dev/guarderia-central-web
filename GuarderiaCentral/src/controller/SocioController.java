package controller;

import dto.GarageDTO;
import dto.SocioDTO;
import dto.VehiculoDTO;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import model.Rol;
import model.Socio;
import model.Usuario;
import service.SocioService;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class SocioController implements Controlador {

    private final SocioService socioService;

    // Patrones de validación
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");
    private static final Pattern PATTERN_DNI = Pattern.compile("^\\d{7,8}$");
    private static final Pattern PATTERN_ID = Pattern.compile("^[0-9]+$");

    public SocioController() {
        this.socioService = new SocioService();
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
        if (dto.getDni() == null || !PATTERN_DNI.matcher(dto.getDni()).matches()) {
            throw new ErrorNegocio("El formato del DNI no es válido.");
        }
        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El formato del nombre no es válido.");
        }
        if (dto.getApellido() == null || !PATTERN_NOMBRE.matcher(dto.getApellido().trim()).matches()) {
            throw new ErrorNegocio("El formato del apellido no es válido.");
        }
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty() || dto.getDireccion().length() > 200) {
            throw new ErrorNegocio("La dirección es obligatoria.");
        }
        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono()).matches()) {
            throw new ErrorNegocio("El formato del teléfono no es válido.");
        }
        if (dto.getNombreUsuario() == null || !PATTERN_USUARIO.matcher(dto.getNombreUsuario()).matches()) {
            throw new ErrorNegocio("El nombre de usuario no es válido.");
        }
        if (dto.getClave() == null || !PATTERN_CLAVE.matcher(dto.getClave()).matches()) {
            throw new ErrorNegocio("La clave no cumple con los requisitos de seguridad.");
        }
        if (dto.getFechaIngreso() == null || dto.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ErrorNegocio("La fecha de ingreso no es válida.");
        }

        socioService.registrarSocio(dto);
    }

    public void modificarSocio(Usuario usuarioSesion, SocioDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);
        if (dto == null || dto.getId() <= 0) {
            throw new ErrorNegocio("El ID del socio no es válido para la modificación.");
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
        return socioService.listarVehiculosPorSocio(socioId);
    }

    public void consultarMiGarage(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);

        String reporte = socioService.obtenerEstadoGarageSocio(socioId);
        System.out.println("--- Estado de mi Garage Propio ---");
        System.out.println(reporte);
        System.out.println("----------------------------------");
    }

    public List<VehiculoDTO> listarVehiculosPorSocio(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);
        return socioService.listarVehiculosPorSocio(socioId);
    }

    public List<GarageDTO> listarGarajesPorSocio(Usuario usuarioSesion, int socioId) {
        validarSocioOAdmin(usuarioSesion);
        validarPermisoSocio(usuarioSesion, socioId);
        return socioService.listarGarajesPorSocio(socioId);
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {}

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarSocioOAdmin(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.SOCIO && usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Acceso denegado.");
        }
    }

    private void validarPermisoSocio(Usuario usuarioSesion, int socioId) {
        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (socio.getId() != socioId) {
                throw new SecurityException("Acceso denegado.");
            }
        }
    }
}