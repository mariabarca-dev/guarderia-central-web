package service;

import dao.GarageDAO;
import dao.ZonaDAO;
import dao.impl.GarageDAOImpl;
import dao.impl.ZonaDAOImpl;
import dto.GarageDTO;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import exceptions.ZonaSinCapacidadException;
import mapper.GarageMapper;
import model.Garage;
import model.Socio;
import model.Zona;
import util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GarageService {

    private final GarageDAO garageDAO;
    private final ZonaDAO zonaDAO;

    public GarageService() {
        this.garageDAO = new GarageDAOImpl();
        this.zonaDAO = new ZonaDAOImpl();
    }

    public void registrarGarage(GarageDTO dto) throws ZonaSinCapacidadException, ErrorNegocio, RegistroNoEncontradoException {
        Zona zona = zonaDAO.buscarPorLetra(dto.getZona());
        if (zona == null) {
            throw new RegistroNoEncontradoException("No existe la zona especificada: " + dto.getZona());
        }

        long garajesEnZona = garageDAO.listarTodos().stream()
                .filter(g -> g.getZona() != null && g.getZona().getLetra().equalsIgnoreCase(dto.getZona()))
                .count();

        // Lanzamiento de la excepción específica ZonaSinCapacidadException
        if (garajesEnZona >= zona.getCapacidadVehiculos()) {
            throw new ZonaSinCapacidadException("La zona '" + dto.getZona() + "' alcanzó su capacidad máxima de "
                    + zona.getCapacidadVehiculos() + " garajes.");
        }

        boolean numeroExistente = garageDAO.listarTodos().stream()
                .anyMatch(g -> g.getNumeroGarage() == dto.getNumeroGarage());
        if (numeroExistente) {
            throw new ErrorNegocio("Ya existe un garaje registrado con el número: " + dto.getNumeroGarage());
        }

        int nuevoId = IdGenerator.obtenerNuevoId("garage", 4000);
        dto.setId(nuevoId);

        // Se pasa 'null' como segundo parámetro para el objeto Socio si se crea sin dueño asignado
        Garage garage = GarageMapper.toModel(dto, null, zona);
        garageDAO.guardar(garage);
    }

    public List<GarageDTO> listarTodos() {
        return garageDAO.listarTodos().stream()
                .map(GarageMapper::toDto)
                .collect(Collectors.toList());
    }

    public void actualizarGarage(GarageDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        Garage existente = garageDAO.buscarPorNumero(dto.getNumeroGarage());
        if (existente == null) {
            throw new RegistroNoEncontradoException("No se encontró el garaje número: " + dto.getNumeroGarage());
        }

        Zona zona = zonaDAO.buscarPorLetra(dto.getZona());
        if (zona == null) {
            throw new RegistroNoEncontradoException("No existe la zona especificada: " + dto.getZona());
        }

        dto.setId(existente.getId());

        // Se preserva el socio propietario que ya tenía el garaje registrado en la base/lista
        Socio socioExistente = existente.getSocioPropietario();
        Garage garageActualizado = GarageMapper.toModel(dto, socioExistente, zona);

        garageDAO.actualizar(garageActualizado);
    }

    public void eliminarGarage(int numeroGarage) throws RegistroNoEncontradoException {
        Garage existente = garageDAO.buscarPorNumero(numeroGarage);
        if (existente == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: El garaje " + numeroGarage + " no existe.");
        }
        garageDAO.eliminar(numeroGarage);
    }

    public List<String> consultarDisponibilidadGarages() {
        List<String> reporte = new ArrayList<>();
        List<Zona> zonas = zonaDAO.listarTodos();

        for (Zona z : zonas) {
            long ocupados = garageDAO.listarTodos().stream()
                    .filter(g -> g.getZona() != null && g.getZona().getLetra().equalsIgnoreCase(z.getLetra()))
                    .count();
            int disponibles = z.getCapacidadVehiculos() - (int) ocupados;
            reporte.add("Zona " + z.getLetra() + " (" + z.getTipoVehiculo() + "): "
                    + disponibles + " disponibles de " + z.getCapacidadVehiculos() + " totales.");
        }
        return reporte;
    }

    public Garage buscarPorId(int id) throws RegistroNoEncontradoException {
        // Asumiendo que tu DAO tiene un método buscarPorId
        Garage g = garageDAO.buscarPorId(id);

        if (g == null) {
            throw new RegistroNoEncontradoException("No se encontró el garage con ID: " + id);
        }

        return g;
    }



}