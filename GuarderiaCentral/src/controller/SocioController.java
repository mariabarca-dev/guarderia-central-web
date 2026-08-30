package controller;

import service.*;
import dto.*;
import exceptions.RegistroNoEncontradoException;
import model.*;
import java.util.List;

public class SocioController {

    private final SocioService socioService = new SocioService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private final PropiedadGarageService propiedadGarageService = new PropiedadGarageService();
    private final AsignacionVehiculoGarageService asignacionVehiculoGarageService = new AsignacionVehiculoGarageService();

    public SocioController(Usuario usuario) {
        if (usuario == null || (usuario.getRol() != Rol.SOCIO && usuario.getRol() != Rol.ADMINISTRADOR)) {
            throw new SecurityException("Acceso denegado: No tienes permisos para acceder a esta sección.");
        }
    }

    /**
     * Retorna la lista de vehículos del socio. La lógica de filtrado ahora vive
     * en el servicio.
     */
    public List<VehiculoDTO> consultarMisVehiculos(int socioId) {
        return vehiculoService.listarPorSocio(socioId);
    }

    /**
     * Retorna un DTO con el estado detallado del garage del socio. Esto evita
     * que el controlador sepa cómo "mapear" o "consultar" estados.
     */
    public void consultarMiGarage(int socioId) {
        String reporte = propiedadGarageService.obtenerEstadoGarageSocio(socioId);
        System.out.println("--- Estado de mi Garage Propio ---");
        System.out.println(reporte);
        System.out.println("----------------------------------");
    }
    
    public SocioDTO buscarSocioPorId(int id) {
        try {
            // Llamamos al servicio, que es quien tiene acceso al DAO
            return socioService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public List<VehiculoDTO> listarVehiculosPorSocio(int socioId) {
        // El servicio de vehículos ya filtra por socio
        return vehiculoService.listarPorSocio(socioId);
    }

    public List<GarageDTO> listarGarajesPorSocio(int socioId) {
        // Asegúrate de que PropiedadGarageService tenga un método para listar
        return propiedadGarageService.listarPorSocio(socioId);
    }
}