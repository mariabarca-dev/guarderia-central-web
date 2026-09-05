package controller;

import dto.ZonaDTO;
import service.ZonaService;
import model.Usuario;
import model.Rol;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;

import java.util.List;

public class ZonaController {

    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    /**
     * Registra una nueva zona.
     * Acceso: Solo ADMINISTRADOR.
     */
    public void registrarZona(Usuario usuarioSesion, ZonaDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new IllegalArgumentException("El objeto DTO no puede ser nulo.");
        }
        if (dto.getLetra() == null || dto.getLetra().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar la letra de la zona.");
        }
        if (dto.getTipoVehiculo() == null || dto.getTipoVehiculo().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar el tipo de vehículo permitido.");
        }
        if (dto.getCapacidadVehiculos() <= 0) {
            throw new IllegalArgumentException("La capacidad de vehículos debe ser mayor a 0.");
        }
        if (dto.getAncho() <= 0 || dto.getLargo() <= 0) {
            throw new IllegalArgumentException("Las dimensiones de ancho y largo deben ser mayores a 0.");
        }

        zonaService.registrarZona(dto);
    }

    /**
     * Lista todas las zonas registradas.
     * Acceso: ADMINISTRADOR, EMPLEADO y SOCIO (Consulta pública/catalogada).
     */
    public List<ZonaDTO> listarZonas(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);
        return zonaService.listarTodas();
    }

    /**
     * Busca una zona por su letra identificadora.
     * Acceso: ADMINISTRADOR, EMPLEADO y SOCIO.
     */
    public ZonaDTO buscarPorLetra(Usuario usuarioSesion, String letra) throws RegistroNoEncontradoException {
        validarUsuarioAutenticado(usuarioSesion);

        if (letra == null || letra.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar una letra de zona válida.");
        }

        return zonaService.buscarPorLetra(letra.trim().toUpperCase());
    }

    /**
     * Actualiza la información de una zona existente.
     * Acceso: Solo ADMINISTRADOR.
     */
    public void actualizarZona(Usuario usuarioSesion, ZonaDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new IllegalArgumentException("El objeto DTO no puede ser nulo.");
        }
        if (dto.getLetra() == null || dto.getLetra().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar la letra de la zona a actualizar.");
        }
        if (dto.getCapacidadVehiculos() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0.");
        }
        if (dto.getAncho() <= 0 || dto.getLargo() <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser mayores a 0.");
        }

        zonaService.actualizarZona(dto);
    }

    /**
     * Elimina una zona por su letra.
     * Acceso: Solo ADMINISTRADOR.
     */
    public void eliminarZona(Usuario usuarioSesion, String letra) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (letra == null || letra.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar una letra de zona válida para eliminar.");
        }

        zonaService.eliminarZona(letra.trim().toUpperCase());
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