package service;

import model.AsignacionEmpleadoZona;
import model.Empleado;
import model.Zona;
import dto.AsignacionEmpleadoZonaDTO;
import mapper.AsignacionEmpleadoZonaMapper;
import dao.AsignacionEmpleadoZonaDAO;
import dao.impl.AsignacionEmpleadoZonaDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.ZonaSinCapacidadException;
import java.util.List;

/**
 * Servicio para gestionar la asignación de empleados a zonas.
 * Ahora utiliza DTOs para la entrada de datos.
 */
public class AsignacionEmpleadoZonaService {
    
    private AsignacionEmpleadoZonaDAO dao;

    public AsignacionEmpleadoZonaService() {
        this.dao = new AsignacionEmpleadoZonaDAOImpl();
    }

    /**
     * Registra una nueva asignación de un empleado a una zona.
     * @param dto El objeto de transferencia de datos con la información de asignación.
     * @throws ErrorNegocio Si los datos son inválidos o la zona no tiene capacidad.
     */
    public void crearAsignacion(AsignacionEmpleadoZonaDTO dto) throws ErrorNegocio {
        // 1. Mapeo: Convertimos el DTO a Modelo para trabajar con la lógica de negocio
        AsignacionEmpleadoZona nuevaAsignacion = AsignacionEmpleadoZonaMapper.toModel(dto);
        
        // 2. Extraemos los datos del modelo para realizar las validaciones
        Empleado empleado = nuevaAsignacion.getEmpleado();
        Zona zona = nuevaAsignacion.getZona();
        int cantVehiculosACargo = nuevaAsignacion.getCantVehiculosACargo();

        // 3. Validaciones
        if (empleado == null || zona == null) {
            throw new ErrorNegocio("Error: El empleado y la zona son obligatorios.");
        }
        
        if (cantVehiculosACargo < 0) {
            throw new ErrorNegocio("Error: La cantidad de vehículos a cargo no puede ser negativa.");
        }

        // Validación de Capacidad
        int vehiculosActuales = dao.contarVehiculosEnZona(zona.getId()); 
        
        if ((vehiculosActuales + cantVehiculosACargo) > zona.getCapacidadVehiculos()) {
            throw new ZonaSinCapacidadException("La zona " + zona.getLetra() + 
                  " no tiene capacidad suficiente para gestionar " + cantVehiculosACargo + " vehículos más.");
        }

        // 4. Persistir el objeto Modelo
        dao.guardar(nuevaAsignacion);
    }

    public List<AsignacionEmpleadoZona> listarTodas() {
        return dao.listarTodas();
    }
    
    public List<AsignacionEmpleadoZona> buscarPorCodigoEmpleado(String codigo) {
        return dao.buscarPorEmpleado(codigo);
    }
}