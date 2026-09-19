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
import exceptions.GarageYaVendidoException;
import java.util.List;
import java.util.stream.Collectors;
import mapper.GarageMapper;
import mapper.PropiedadGarageMapper;
import model.AsignacionVehiculoGarage;

public class PropiedadGarageService {

    private final PropiedadGarageDAO dao;
    private final SocioDAO socioDAO;
    private final GarageDAO garageDAO;
    private final AsignacionVehiculoGarageService asignacionService;

    public PropiedadGarageService() {
        this.dao = new PropiedadGarageDAOImpl();
        this.socioDAO = new SocioDAOImpl();
        this.garageDAO = new GarageDAOImpl();
        this.asignacionService = new AsignacionVehiculoGarageService();
    }

    public void registrarPropiedad(PropiedadGarageDTO dto) throws ErrorNegocio {
        Socio socio = socioDAO.buscarPorId(dto.getSocio().getId());
        Garage garage = garageDAO.buscarPorNumero(dto.getGarage().getNumeroGarage());

        if (socio == null || garage == null) {
            throw new ErrorNegocio("Error de negocio: El socio o el garaje especificados no existen en el sistema.");
        }

        // REGLA DE NEGOCIO: Excepción específica si el garaje ya tiene dueño
        if (garage.getSocioPropietario() != null) {
            throw new GarageYaVendidoException("Error: El garaje N° " + garage.getNumeroGarage()
                    + " ya tiene un socio propietario asignado.");
        }

        PropiedadGarage nuevaPropiedad = PropiedadGarageMapper.toModel(dto, socio, garage);

        if (nuevaPropiedad.getFechaCompraGarage().isBefore(socio.getFechaIngreso())) {
            throw new ErrorNegocio("Error de negocio: La fecha de compra (" + nuevaPropiedad.getFechaCompraGarage()
                    + ") no puede ser anterior a la fecha de ingreso del socio (" + socio.getFechaIngreso() + ").");
        }

        dao.guardar(nuevaPropiedad);
    }

    public Garage buscarGaragePorSocioId(int socioId) {
        PropiedadGarage propiedad = dao.buscarPorSocio(socioId);
        return (propiedad != null) ? propiedad.getGarage() : null;
    }

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
        return garageDAO.listarPorSocio(socioId).stream()
                .map(GarageMapper::toDto)
                .collect(Collectors.toList());
    }
}