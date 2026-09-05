package controller;

import dto.AsignacionVehiculoGarageDTO;
import service.AsignacionVehiculoGarageService;
import model.Usuario;
import model.Socio;
import model.Rol;
import model.AsignacionVehiculoGarage;
import exceptions.ErrorNegocio;

import java.time.LocalDate;
import java.util.List;

public class AsignacionVehiculoGarageController {

    private final AsignacionVehiculoGarageService asignacionService;

    public AsignacionVehiculoGarageController(AsignacionVehiculoGarageService asignacionService) {
        this.asignacionService = asignacionService;
    }

    /**
     * Registra la asignación de un vehículo a un garaje.
     * Acceso: Solo ADMINISTRADOR.
     */
    public void crearAsignacion(Usuario usuarioSesion, AsignacionVehiculoGarageDTO dto) throws ErrorNegocio {
        validarAdministrador(usuarioSesion);

        if (dto == null) {
            throw new IllegalArgumentException("El objeto DTO no puede ser nulo.");
        }
        if (dto.getVehiculo() == null || dto.getVehiculo().getId() <= 0) {
            throw new IllegalArgumentException("Debe especificar un vehículo válido para realizar la asignación.");
        }
        if (dto.getGarage() == null || dto.getGarage().getId() <= 0) {
            throw new IllegalArgumentException("Debe especificar un garaje válido para realizar la asignación.");
        }
        if (dto.getFechaAsignacionGarage() == null) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser nula.");
        }
        if (dto.getFechaAsignacionGarage().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de asignación no puede ser una fecha futura.");
        }

        asignacionService.crearAsignacion(dto);
    }

    /**
     * Lista todas las asignaciones de vehículos a garajes registradas.
     * Acceso: ADMINISTRADOR y EMPLEADO.
     */
    public List<AsignacionVehiculoGarage> listarTodas(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);

        if (usuarioSesion.getRol() == Rol.SOCIO) {
            throw new SecurityException("Acceso denegado: Los socios no tienen permiso para ver el listado global de asignaciones.");
        }

        return asignacionService.listarTodas();
    }

    /**
     * Busca la asignación correspondiente a un vehículo específico.
     * Acceso: ADMINISTRADOR, EMPLEADO o el SOCIO dueño del vehículo.
     */
    public AsignacionVehiculoGarage buscarPorVehiculo(Usuario usuarioSesion, int vehiculoId) {
        validarUsuarioAutenticado(usuarioSesion);

        if (vehiculoId <= 0) {
            throw new IllegalArgumentException("El ID de vehículo proporcionado no es válido.");
        }

        AsignacionVehiculoGarage asignacion = asignacionService.buscarPorVehiculo(vehiculoId);

        // Control de Privacidad: Si el usuario es un SOCIO, validar que el vehículo asignado le pertenezca usando getSocioId()
        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (asignacion != null && asignacion.getVehiculo() != null) {
                if (asignacion.getVehiculo().getSocioId() != socio.getId()) {
                    throw new SecurityException("Acceso denegado: Solo puede consultar la asignación de sus propios vehículos.");
                }
            }
        }

        return asignacion;
    }

    // --- Métodos Privados de Seguridad ---

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