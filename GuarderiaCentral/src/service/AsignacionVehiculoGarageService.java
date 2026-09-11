package service;

import model.AsignacionVehiculoGarage;
import model.Vehiculo;
import model.Garage;
import model.Zona;
import model.Socio;
import dto.AsignacionVehiculoGarageDTO;
import dto.GarageDTO;
import dto.VehiculoDTO;
import mapper.AsignacionVehiculoGarageMapper;
import mapper.GarageMapper;
import mapper.VehiculoMapper;
import dao.AsignacionVehiculoGarageDAO;
import dao.impl.AsignacionVehiculoGarageDAOImpl;
import dao.SocioDAO;
import dao.impl.SocioDAOImpl;
import dao.ZonaDAO;
import dao.impl.ZonaDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.GarageYaOcupadoException;
import exceptions.RegistroNoEncontradoException;
import exceptions.ZonaSinCapacidadException;

import java.util.List;

public class AsignacionVehiculoGarageService {

    private final AsignacionVehiculoGarageDAO dao;
    private final SocioDAO socioDAO;
    private final ZonaDAO zonaDAO;

    public AsignacionVehiculoGarageService() {
        this.dao = new AsignacionVehiculoGarageDAOImpl();
        this.socioDAO = new SocioDAOImpl();
        this.zonaDAO = new ZonaDAOImpl();
    }

    /**
     * Registra la asignación de un vehículo a un garaje.
     * Contiene las reglas de negocio de ocupación, pertenencia, compatibilidad y fechas.
     */
    public void crearAsignacion(AsignacionVehiculoGarageDTO dto)
            throws ErrorNegocio, GarageYaOcupadoException, ZonaSinCapacidadException {

        VehiculoService vehiculoService = new VehiculoService();
        GarageService garageService = new GarageService();

        // 1. REGLA DE NEGOCIO: Recuperar entidades desde sus respectivos servicios
        Vehiculo vehiculoCompleto;
        Garage garageCompleto;
        try {
            VehiculoDTO vDto = vehiculoService.buscarPorId(dto.getVehiculo().getId());
            vehiculoCompleto = VehiculoMapper.toModel(vDto);

            // garageService.buscarPorId ya devuelve directamente un objeto Garage
            garageCompleto = garageService.buscarPorId(dto.getGarage().getId());

        } catch (RegistroNoEncontradoException e) {
            throw new ErrorNegocio("Error de negocio: No se puede realizar la asignación. " + e.getMessage());
        }

        // 2. REGLA DE NEGOCIO: Excepción específica si el garaje ya está ocupado por un vehículo
        if (dao.buscarPorGarage(garageCompleto) != null) {
            throw new GarageYaOcupadoException("Error: El garaje N° " + garageCompleto.getNumeroGarage()
                    + " ya se encuentra ocupado por otro vehículo.");
        }

        // 3. REGLA DE NEGOCIO: El vehículo no puede tener otra asignación activa
        if (buscarPorVehiculo(vehiculoCompleto.getId()) != null) {
            throw new ErrorNegocio("Error de negocio: El vehículo con matrícula " + vehiculoCompleto.getMatricula()
                    + " ya está asignado a un garaje en el sistema.");
        }

        // 4. REGLA DE NEGOCIO: El vehículo debe pertenecer al socio dueño del garaje
        if (garageCompleto.getSocioPropietario() != null) {
            if (vehiculoCompleto.getSocioId() != garageCompleto.getSocioPropietario().getId()) {
                throw new ErrorNegocio("Error de negocio: El vehículo no pertenece al socio propietario de este garaje.");
            }
        }

        // 5. Mapeo a Modelo
        AsignacionVehiculoGarage nuevaAsignacion = AsignacionVehiculoGarageMapper.toModel(dto, garageCompleto, vehiculoCompleto);

        // 6. REGLA DE NEGOCIO: Compatibilidad de tipo de vehículo con el tipo de la Zona
        if (vehiculoCompleto.getTipo() != garageCompleto.getZona().getTipoVehiculo()) {
            throw new ErrorNegocio("Error de negocio: El vehículo de tipo " + vehiculoCompleto.getTipo()
                    + " no es compatible con la zona asignada a " + garageCompleto.getZona().getTipoVehiculo() + ".");
        }

        // 7. REGLA DE NEGOCIO: Consistencia de fechas (Asignación vs Compra)
        if (garageCompleto.getFechaCompra() != null) {
            if (nuevaAsignacion.getFechaAsignacionGarage().isBefore(garageCompleto.getFechaCompra())) {
                throw new ErrorNegocio("Error de negocio: La fecha de asignación no puede ser anterior a la fecha de compra del garaje ("
                        + garageCompleto.getFechaCompra() + ").");
            }
        }

        // 8. REGLA DE NEGOCIO: Excepción específica de capacidad máxima en la Zona
        Zona zona = garageCompleto.getZona();
        int capacidadMaxima = zona.getCapacidadVehiculos();
        int vehiculosActuales = contarVehiculosActivosEnZona(zona);

        if (vehiculosActuales >= capacidadMaxima) {
            throw new ZonaSinCapacidadException("Error: La zona '" + zona.getLetra()
                    + "' ha alcanzado su capacidad máxima permitida de " + capacidadMaxima + " vehículos.");
        }

        // 9. Persistir asignación
        dao.guardar(nuevaAsignacion);
    }
    private int contarVehiculosActivosEnZona(Zona zona) {
        List<AsignacionVehiculoGarage> todas = dao.listarTodas();
        int contador = 0;
        for (AsignacionVehiculoGarage asignacion : todas) {
            if (asignacion.getGarage() != null && asignacion.getGarage().getZona() != null) {
                if (asignacion.getGarage().getZona().getId() == zona.getId()) {
                    contador++;
                }
            }
        }
        return contador;
    }

    private Socio buscarSocioExistente(String identificadorSocio) {
        if (identificadorSocio == null || identificadorSocio.trim().isEmpty() || identificadorSocio.equalsIgnoreCase("Libre")) {
            return null;
        }

        Socio porDni = socioDAO.buscarPorDni(identificadorSocio);
        if (porDni != null) {
            return porDni;
        }

        return socioDAO.listarTodos().stream()
                .filter(s -> s.getNombre() != null && s.getNombre().equalsIgnoreCase(identificadorSocio))
                .findFirst()
                .orElse(null);
    }

    public AsignacionVehiculoGarage buscarPorGarage(Garage garage) {
        return dao.buscarPorGarage(garage);
    }

    public List<AsignacionVehiculoGarage> listarTodas() {
        return dao.listarTodas();
    }

    public AsignacionVehiculoGarage buscarPorVehiculo(int vehiculoId) {
        return dao.listarTodas().stream()
                .filter(a -> a.getVehiculo().getId() == vehiculoId)
                .findFirst()
                .orElse(null);
    }
}