package service;

import exceptions.MatriculaDuplicadaException;
import model.Vehiculo;
import model.TipoVehiculo;
import dto.VehiculoDTO;
import mapper.VehiculoMapper;
import dao.VehiculoDAO;
import dao.impl.VehiculoDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

public class VehiculoService {

    private final VehiculoDAO vehiculoDAO;

    public VehiculoService() {
        this.vehiculoDAO = new VehiculoDAOImpl();
    }

    public void registrarVehiculo(VehiculoDTO dto) throws ErrorNegocio, MatriculaDuplicadaException {
        if (dto == null) {
            throw new ErrorNegocio("Error: El vehículo no puede ser nulo.");
        }

        if (vehiculoDAO.buscarPorMatricula(dto.getMatricula()) != null) {
            throw new MatriculaDuplicadaException("Error: Ya existe un vehículo con matrícula: " + dto.getMatricula());
        }

        int nuevoId = IdGenerator.obtenerNuevoId("vehiculo", 2000);
        dto.setId(nuevoId);

        Vehiculo vehiculo = VehiculoMapper.toModel(dto);
        vehiculoDAO.guardar(vehiculo);
    }

    public VehiculoDTO buscarPorId(int id) throws RegistroNoEncontradoException {
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v == null) {
            throw new RegistroNoEncontradoException("No se encontró vehículo con ID: " + id);
        }
        return VehiculoMapper.toDto(v);
    }

    public VehiculoDTO buscarPorMatricula(String matricula) throws RegistroNoEncontradoException {
        Vehiculo v = vehiculoDAO.buscarPorMatricula(matricula);
        if (v == null) {
            throw new RegistroNoEncontradoException("No se encontró vehículo con matrícula: " + matricula);
        }
        return VehiculoMapper.toDto(v);
    }

    public List<VehiculoDTO> listarTodos() {
        return vehiculoDAO.listarTodos().stream()
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> listarPorSocio(int socioId) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getSocioId() == socioId)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> buscarVehiculosPorSocio(int socioId) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getSocioId() == socioId)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> buscarPorTipo(TipoVehiculo tipo) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getTipo() == tipo)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> listarVehiculosPorResponsable(int empleadoId) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getEmpleadoId() == empleadoId)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public void actualizarVehiculo(VehiculoDTO dto) throws RegistroNoEncontradoException, ErrorNegocio, MatriculaDuplicadaException {
        if (vehiculoDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No existe vehículo con ID " + dto.getId());
        }

        Vehiculo existentePorMatricula = vehiculoDAO.buscarPorMatricula(dto.getMatricula());
        if (existentePorMatricula != null && existentePorMatricula.getId() != dto.getId()) {
            throw new MatriculaDuplicadaException("Error: Ya existe otro vehículo con la matrícula: " + dto.getMatricula());
        }

        Vehiculo vehiculo = VehiculoMapper.toModel(dto);
        vehiculoDAO.actualizar(vehiculo);
    }

    public void eliminarVehiculo(String matricula) throws RegistroNoEncontradoException {
        if (vehiculoDAO.buscarPorMatricula(matricula) == null) {
            throw new RegistroNoEncontradoException("No existe vehículo con matrícula " + matricula);
        }
        vehiculoDAO.eliminar(matricula);
    }
}