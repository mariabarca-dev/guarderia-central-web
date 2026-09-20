package controller;

import dto.ZonaDTO;
import service.ZonaService;
import model.Usuario;
import model.Rol;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;

import java.util.List;
import java.util.regex.Pattern;

public class ZonaController {

    private final ZonaService zonaService;

    private static final Pattern PATTERN_LETRA_ZONA = Pattern.compile("^[a-zA-Z]{1,3}$");
    private static final Pattern PATTERN_TIPO_VEHICULO = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,50}$");
    private static final Pattern PATTERN_NUMERO_POSITIVO = Pattern.compile("^[0-9]+$");

    public ZonaController() {
        this.zonaService = new ZonaService();
    }

    public void registrarZona(Usuario usuarioSesion, ZonaDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El objeto DTO no puede ser nulo.");
        }

        if (dto.getLetra() == null || !PATTERN_LETRA_ZONA.matcher(dto.getLetra().trim()).matches()) {
            throw new ErrorNegocio("Error de formato: La letra de la zona no es válida (debe tener entre 1 y 3 caracteres alfabéticos).");
        }

        if (dto.getTipoVehiculo() == null || !PATTERN_TIPO_VEHICULO.matcher(dto.getTipoVehiculo().trim()).matches()) {
            throw new ErrorNegocio("Error de formato: El tipo de vehículo permitido no es válido (3 a 50 caracteres alfabéticos).");
        }

        if (dto.getCapacidadVehiculos() <= 0 || !PATTERN_NUMERO_POSITIVO.matcher(String.valueOf(dto.getCapacidadVehiculos())).matches()) {
            throw new ErrorNegocio("Error de formato: La capacidad de vehículos debe ser un número entero mayor a 0.");
        }

        if (dto.getAncho() <= 0 || dto.getLargo() <= 0) {
            throw new ErrorNegocio("Error de formato: Las dimensiones de ancho y largo deben ser valores numéricos mayores a 0.");
        }

        dto.setLetra(dto.getLetra().trim().toUpperCase());
        dto.setTipoVehiculo(dto.getTipoVehiculo().trim());

        zonaService.registrarZona(dto);
    }

    public List<ZonaDTO> listarZonas(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);
        return zonaService.listarTodas();
    }

    public ZonaDTO buscarPorLetra(Usuario usuarioSesion, String letra) throws RegistroNoEncontradoException, ErrorNegocio {
        validarUsuarioAutenticado(usuarioSesion);

        if (letra == null || !PATTERN_LETRA_ZONA.matcher(letra.trim()).matches()) {
            throw new ErrorNegocio("Error de formato: Debe proporcionar una letra de zona válida.");
        }

        return zonaService.buscarPorLetra(letra.trim().toUpperCase());
    }

    public void actualizarZona(Usuario usuarioSesion, ZonaDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new ErrorNegocio("El objeto DTO no puede ser nulo.");
        }

        if (dto.getLetra() == null || !PATTERN_LETRA_ZONA.matcher(dto.getLetra().trim()).matches()) {
            throw new ErrorNegocio("Error de formato: La letra de la zona a actualizar no es válida.");
        }

        if (dto.getCapacidadVehiculos() <= 0 || !PATTERN_NUMERO_POSITIVO.matcher(String.valueOf(dto.getCapacidadVehiculos())).matches()) {
            throw new ErrorNegocio("Error de formato: La capacidad debe ser un número entero mayor a 0.");
        }

        if (dto.getAncho() <= 0 || dto.getLargo() <= 0) {
            throw new ErrorNegocio("Error de formato: Las dimensiones deben ser mayores a 0.");
        }

        dto.setLetra(dto.getLetra().trim().toUpperCase());

        zonaService.actualizarZona(dto);
    }

    public void eliminarZona(Usuario usuarioSesion, String letra) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (letra == null || !PATTERN_LETRA_ZONA.matcher(letra.trim()).matches()) {
            throw new ErrorNegocio("Error de formato: Debe especificar una letra de zona válida para eliminar.");
        }

        zonaService.eliminarZona(letra.trim().toUpperCase());
    }

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