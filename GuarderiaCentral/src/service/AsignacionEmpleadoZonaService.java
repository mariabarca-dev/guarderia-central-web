package service;

import dto.EmpleadoDTO;
import dto.ZonaDTO;
import model.AsignacionEmpleadoZona;
import model.Zona;
import dto.AsignacionEmpleadoZonaDTO;
import mapper.AsignacionEmpleadoZonaMapper;
import dao.AsignacionEmpleadoZonaDAO;
import dao.impl.AsignacionEmpleadoZonaDAOImpl;
import exceptions.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la asignación de empleados a zonas.
 * Ahora utiliza DTOs para la entrada de datos.
 */
public class AsignacionEmpleadoZonaService {

    private AsignacionEmpleadoZonaDAO dao;
    private final ZonaService zonaService = new ZonaService();
    private final EmpleadoService empleadoService;

    public AsignacionEmpleadoZonaService() {
        this.dao = new AsignacionEmpleadoZonaDAOImpl();
        this.empleadoService = new EmpleadoService();
    }

    /**
     * Crea la asignación de un empleado a una zona.
     * Reglas de negocio: la cantidad de vehículos no puede ser negativa, un empleado
     * no puede asignarse dos veces a la misma zona y la zona no puede superar su capacidad.
     */
    public void crearAsignacion(AsignacionEmpleadoZonaDTO dto) throws ErrorNegocio {
        if (dto == null || dto.getEmpleado() == null || dto.getZona() == null) {
            throw new ErrorNegocio("Error: El empleado y la zona son obligatorios.");
        }

        AsignacionEmpleadoZona nuevaAsignacion = AsignacionEmpleadoZonaMapper.toModel(dto);
        Zona zona = nuevaAsignacion.getZona();
        int cantVehiculosACargo = nuevaAsignacion.getCantVehiculosACargo();

        if (cantVehiculosACargo < 0) {
            throw new ErrorNegocio("Error: La cantidad de vehículos a cargo no puede ser negativa.");
        }

        // Un empleado puede estar en varias zonas, pero no dos veces en la misma
        int empleadoId = dto.getEmpleado().getId();
        boolean yaAsignado = dao.listarTodas().stream()
                .anyMatch(a -> a.getEmpleado() != null && a.getZona() != null
                        && a.getEmpleado().getId() == empleadoId
                        && a.getZona().getId() == zona.getId());
        if (yaAsignado) {
            throw new ErrorNegocio("Error: El empleado ya está asignado a la zona " + zona.getLetra() + ".");
        }

        int vehiculosActuales = dao.contarVehiculosEnZona(zona.getId());

        if ((vehiculosActuales + cantVehiculosACargo) > zona.getCapacidadVehiculos()) {
            throw new ZonaSinCapacidadException("La zona " + zona.getLetra() +
                    " no tiene capacidad suficiente para gestionar " + cantVehiculosACargo + " vehículos más.");
        }

        dao.guardar(nuevaAsignacion);
    }

    public List<AsignacionEmpleadoZona> listarTodas() {
        return dao.listarTodas();
    }

    public List<AsignacionEmpleadoZona> buscarPorCodigoEmpleado(String codigo) {
        return dao.buscarPorEmpleado(codigo);
    }

    public void asignarEmpleadoAZona(AsignacionEmpleadoZonaDTO dto) throws ErrorNegocio {
        // Validation: zona exista
        ZonaDTO zona = zonaService.listarTodas().stream()
                .filter(z -> z.getId() == dto.getZona().getId())
                .findFirst()
                .orElseThrow(() -> new RegistroNoEncontradoException("La zona especificada no existe."));

        // Validation: empleado exista
        EmpleadoDTO empleado = empleadoService.buscarEmpleadoPorId(dto.getEmpleado().getId());
        if (empleado == null) {
            throw new RegistroNoEncontradoException("El empleado especificado no existe.");
        }

        // Delegamos al service la creación
        crearAsignacion(dto);
    }

    /**
     * Devuelve la lista de EmpleadoDTO asignados a una zona determinada.
     */
    public List<EmpleadoDTO> obtenerEmpleadosPorZona(int idZona) {
        return listarTodas().stream()
                .filter(asg -> asg.getZona() != null && asg.getZona().getId() == idZona)
                .map(AsignacionEmpleadoZona::getEmpleado)
                .filter(emp -> emp != null)
                .map(mapper.EmpleadoMapper::toDto)
                .collect(Collectors.toList());
    }
}