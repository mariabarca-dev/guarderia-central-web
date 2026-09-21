package service;

import exceptions.CodigoEmpleadoDuplicadoException;
import model.Empleado;
import dto.EmpleadoDTO;
import dto.ZonaDTO;
import dto.VehiculoDTO;
import mapper.AsignacionEmpleadoZonaMapper;
import mapper.EmpleadoMapper;
import dao.EmpleadoDAO;
import dao.impl.EmpleadoDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

public class EmpleadoService {

    private EmpleadoDAO empleadoDAO;
    private final AsignacionEmpleadoZonaService asignacionService;
    private final VehiculoService vehiculoService;

    // Constructor por defecto
    public EmpleadoService() {
        this.empleadoDAO = new EmpleadoDAOImpl();
        this.vehiculoService = new VehiculoService();
        // Inyecta 'this' en la creación del servicio de asignación
        this.asignacionService = new AsignacionEmpleadoZonaService(this);
    }

    // Constructor secundario para evitar la recursión infinita
    public EmpleadoService(AsignacionEmpleadoZonaService asignacionService) {
        this.empleadoDAO = new EmpleadoDAOImpl();
        this.vehiculoService = new VehiculoService();
        this.asignacionService = asignacionService;
    }

    public void registrarEmpleado(EmpleadoDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El objeto empleado no puede ser nulo.");
        }

        if (empleadoDAO.buscarPorCodigo(dto.getCodigo()) != null) {
            throw new CodigoEmpleadoDuplicadoException("Error: Ya existe un empleado registrado con el código: " + dto.getCodigo());
        }

        int nuevoId = IdGenerator.obtenerNuevoId("empleado", 300);
        dto.setId(nuevoId);

        Empleado empleadoModelo = EmpleadoMapper.toModel(dto);
        empleadoDAO.guardar(empleadoModelo);
    }

    public List<EmpleadoDTO> listarTodos() {
        return empleadoDAO.listarTodos().stream()
                .map(EmpleadoMapper::toDto)
                .collect(Collectors.toList());
    }

    public void actualizarEmpleado(EmpleadoDTO dto) throws RegistroNoEncontradoException {
        if (empleadoDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: No se encontró un empleado con ID " + dto.getId());
        }
        empleadoDAO.actualizar(EmpleadoMapper.toModel(dto));
    }

    public void eliminarEmpleado(int id) throws RegistroNoEncontradoException {
        if (empleadoDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: No se encontró un empleado con ID " + id);
        }
        empleadoDAO.eliminar(id);
    }

    public EmpleadoDTO buscarEmpleadoPorId(int idEmpleado) {
        List<EmpleadoDTO> empleados = listarTodos();
        for (EmpleadoDTO e : empleados) {
            if (e.getId() == idEmpleado) {
                return e;
            }
        }
        return null;
    }

    public List<ZonaDTO> listarZonasAsignadas(int empleadoId) {
        return asignacionService.listarTodas().stream()
                .filter(asig -> asig.getEmpleado() != null && asig.getEmpleado().getId() == empleadoId)
                .map(AsignacionEmpleadoZonaMapper::toDto)
                .map(dto -> dto.getZona())
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> listarVehiculosBajoResponsabilidad(int empleadoId) {
        return vehiculoService.listarVehiculosPorResponsable(empleadoId);
    }
}