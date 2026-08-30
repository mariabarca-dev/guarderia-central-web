package service;

import model.Garage;
import dao.GarageDAO;
import dao.SocioDAO;
import dao.ZonaDAO;
import dao.impl.GarageDAOImpl;
import dao.impl.SocioDAOImpl;
import dao.impl.ZonaDAOImpl;
import dto.GarageDTO;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mapper.GarageMapper;
import model.AsignacionVehiculoGarage;
import model.Socio;
import model.Zona;
import util.IdGenerator;

public class GarageService {

    private GarageDAO garageDAO;
    private SocioDAO socioDAO;
    private ZonaDAO zonaDAO;
    //private AsignacionVehiculoGarageService asignacionService;

    public GarageService() {
        this.garageDAO = new GarageDAOImpl();
        this.socioDAO = new SocioDAOImpl();
        this.zonaDAO = new ZonaDAOImpl();
        //this.asignacionService = new AsignacionVehiculoGarageService();
    }

    public void registrarGarage(GarageDTO dto) throws ErrorNegocio {
        // 1. Recuperamos los objetos modelo necesarios (Socio, Zona)
        Socio socio = null;
        if (dto.getSocioPropietario() != null && !dto.getSocioPropietario().equals("Libre")) {
            socio = socioDAO.buscarPorDni(dto.getSocioPropietario());
        }

        Zona zona = zonaDAO.buscarPorLetra(dto.getZona());

        if (zona == null) {
            throw new ErrorNegocio("La zona especificada no existe.");
        }

        // 2. Generar ID
        int nuevoId = IdGenerator.obtenerNuevoId("garage", 1000);

        // 3. Seteamos el ID en el DTO para que el mapper lo use
        dto.setId(nuevoId);

        // 4. Mapear DTO a Modelo (usando el constructor corregido del mapper)
        Garage garage = GarageMapper.toModel(dto, socio, zona);

        // 5. Guardar
        garageDAO.guardar(garage);
    }

    public Garage buscarPorNumero(int numeroGarage) throws RegistroNoEncontradoException {
        Garage g = garageDAO.buscarPorNumero(numeroGarage);
        if (g == null) {
            throw new RegistroNoEncontradoException("No se encontró el garage número: " + numeroGarage);
        }
        return g;
    }

    public List<GarageDTO> listarTodos() {
        return garageDAO.listarTodos().stream()
                .map(GarageMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un garage existente a partir de un DTO.
     *
     * @param dto El DTO con los datos actualizados (debe contener el ID
     * correcto).
     * @throws RegistroNoEncontradoException Si el garage no existe.
     */
    public void actualizarGarage(GarageDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        // 1. Verificamos que el garage existe (usando el ID del DTO)
        if (garageDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: El garage con ID " + dto.getId() + " no existe.");
        }

        // 2. Buscamos los objetos relacionados (Socio y Zona)
        Socio socio = null;
        if (dto.getSocioPropietario() != null && !dto.getSocioPropietario().equals("Libre")) {
            socio = socioDAO.buscarPorDni(dto.getSocioPropietario());
        }
        Zona zona = zonaDAO.buscarPorLetra(dto.getZona());
        if (zona == null) {
            throw new ErrorNegocio("La zona especificada no existe.");
        }

        // 3. Convertimos el DTO a Modelo para la actualización
        Garage garage = GarageMapper.toModel(dto, socio, zona);

        // 4. Actualizar
        garageDAO.actualizar(garage);
    }

    public void eliminarGarage(int numeroGarage) throws RegistroNoEncontradoException {
        if (garageDAO.buscarPorNumero(numeroGarage) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: El garage no existe.");
        }
        garageDAO.eliminar(numeroGarage);
    }

    public Garage buscarPorId(int id) throws RegistroNoEncontradoException {
        // Asumiendo que tu DAO tiene un método buscarPorId
        Garage g = garageDAO.buscarPorId(id);

        if (g == null) {
            throw new RegistroNoEncontradoException("No se encontró el garage con ID: " + id);
        }

        return g;
    }

    public List<String> consultarDisponibilidadGarages() {
        // 1. Obtenemos los datos necesarios de los DAOs/Servicios
        
        AsignacionVehiculoGarageService asignacionService = new AsignacionVehiculoGarageService();
        
        List<Garage> todosLosGarages = garageDAO.listarTodos();
        List<AsignacionVehiculoGarage> todasLasAsignaciones = asignacionService.listarTodas();

        // 2. Contamos cuántos vehículos hay asignados por cada ID de Zona
        // En GarageService.java, ajusta el filtro del Stream:
        Map<Integer, Long> conteoPorZona = todasLasAsignaciones.stream()
                // Agregamos chequeo de null para evitar que explote
                .filter(a -> a.getGarage() != null && a.getGarage().getZona() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getGarage().getZona().getId(),
                        Collectors.counting()
                ));

        // 3. Generamos el reporte comparando contra la capacidad definida en la Zona
        return todosLosGarages.stream().map(g -> {
            Zona zona = g.getZona();
            if (zona == null) {
                return "Garage " + g.getNumeroGarage() + " - Zona: Sin asignar";
            }

            long asignados = conteoPorZona.getOrDefault(zona.getId(), 0L);
            int capacidad = zona.getCapacidadVehiculos();

            String estado = (asignados < capacidad) ? "Disponible" : "Lleno";

            return "Garage " + g.getNumeroGarage()
                    + " | Zona " + zona.getLetra()
                    + ": " + asignados + "/" + capacidad
                    + " [" + estado + "]";
        }).collect(Collectors.toList());
    }
}
