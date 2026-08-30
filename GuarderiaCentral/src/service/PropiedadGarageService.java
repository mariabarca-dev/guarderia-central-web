package service;

import dao.GarageDAO;
import model.PropiedadGarage;
import model.Socio;
import model.Garage;
import dao.PropiedadGarageDAO;
import dao.SocioDAO;
import dao.impl.GarageDAOImpl;
import dao.impl.PropiedadGarageDAOImpl;
import dao.impl.SocioDAOImpl;
import dto.GarageDTO;
import dto.PropiedadGarageDTO;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import mapper.GarageMapper;
import mapper.PropiedadGarageMapper;
import model.AsignacionVehiculoGarage;

/**
 * Servicio para gestionar la propiedad de garajes por parte de los socios.
 * Implementa las reglas de validación de fechas de ingreso y compra.
 */
public class PropiedadGarageService {

    private PropiedadGarageDAO dao;
    private SocioDAO socioDAO;
    private GarageDAO garageDAO;
    private AsignacionVehiculoGarageService asignacionService;

 public PropiedadGarageService() {
    this.dao = new PropiedadGarageDAOImpl();
    // DEBES inicializar los DAOs aquí
    this.socioDAO = new SocioDAOImpl();
    this.garageDAO = new GarageDAOImpl();
    this.asignacionService = new AsignacionVehiculoGarageService();
}

    /**
     * Registra la propiedad de un garaje por parte de un socio.
     *
     * @param socio El socio propietario.
     * @param garage El garaje adquirido.
     * @param fechaCompra La fecha en que el socio compró el garaje.
     * @throws ErrorNegocio Si la fecha de compra es anterior a la fecha de
     * ingreso del socio.
     */
    public void registrarPropiedad(PropiedadGarageDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("Error: Datos del DTO incompletos.");
        }

        // 1. BUSCAR LAS ENTIDADES REALES (Crucial)
        Socio socio = socioDAO.buscarPorId(dto.getSocio().getId());
        Garage garage = garageDAO.buscarPorNumero(dto.getGarage().getNumeroGarage());

        if (socio == null || garage == null) {
            throw new ErrorNegocio("Error: Socio o Garaje no encontrados.");
        }

        // 2. USAR EL MAPPER PASANDO LAS ENTIDADES
        PropiedadGarage nuevaPropiedad = PropiedadGarageMapper.toModel(dto, socio, garage);

        // 3. VALIDAR FECHA
        if (nuevaPropiedad.getFechaCompraGarage().isBefore(socio.getFechaIngreso())) {
            throw new ErrorNegocio("Error: La fecha de compra no puede ser anterior a la fecha de ingreso del socio.");
        }

        // 4. PERSISTIR
        dao.guardar(nuevaPropiedad);
    }

    /**
     * Busca qué garaje posee un socio específico.
     *
     * @param socioId El ID del socio.
     * @return El objeto Garage encontrado o null si no posee.
     */
    public Garage buscarGaragePorSocioId(int socioId) {
        PropiedadGarage propiedad = dao.buscarPorSocio(socioId);
        return (propiedad != null) ? propiedad.getGarage() : null;
    }

    /**
     * Lista todas las relaciones de propiedad registradas.
     */
    public List<PropiedadGarageDTO> listarTodas() {
        return dao.listarTodas().stream()
                .map(PropiedadGarageMapper::toDto)
                .collect(Collectors.toList());
    }

    public String obtenerEstadoGarageSocio(int socioId) {

        Garage g = buscarGaragePorSocioId(socioId);
        if (g == null) {
            return "No posee ningún garage registrado como propiedad.";
        }

        AsignacionVehiculoGarage a = asignacionService.buscarPorGarage(g);

        StringBuilder sb = new StringBuilder();
        sb.append("Garage Nro: ").append(g.getNumeroGarage()).append("\n");

        if (a != null) {
            sb.append("Estado: OCUPADO\n");
            sb.append("Vehículo en garage: ").append(a.getVehiculo().getMatricula())
                    .append(" (").append(a.getVehiculo().getNombre()).append(")");
        } else {
            sb.append("Estado: LIBRE\n");
            sb.append("Puede asignar un vehículo a su garage.");
        }

        return sb.toString();
    }
    
    public List<GarageDTO> listarPorSocio(int socioId) {
    // Implementación que busca garajes asociados a ese socio
    // y los mapea a una lista de GarageDTO
    return garageDAO.listarPorSocio(socioId).stream()
                    .map(GarageMapper::toDto)
                    .collect(Collectors.toList());
}
}