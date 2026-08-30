package service;

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

/**
 * Servicio para gestionar la lógica de negocio de los vehículos.
 * Utiliza DTO y Mapper para la comunicación con capas superiores.
 */
public class VehiculoService {
    
    private VehiculoDAO vehiculoDAO;

    public VehiculoService() {
        this.vehiculoDAO = new VehiculoDAOImpl();
    }

    /**
     * Registra un nuevo vehículo.
     * Se encarga de generar el ID, setearlo en el DTO y convertir a modelo.
     * @param dto El objeto DTO con los datos del nuevo vehículo.
     * @throws exceptions.ErrorNegocio Si la matrícula ya existe o datos nulos.
     */
    public void registrarVehiculo(VehiculoDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("Error: El vehículo no puede ser nulo.");
        }
        
        // 1. Validación: Matrícula única
        if (vehiculoDAO.buscarPorMatricula(dto.getMatricula()) != null) {
            throw new ErrorNegocio("Error: Ya existe un vehículo con matrícula: " + dto.getMatricula());
        }
        
        // 2. Generar ID para la nueva entidad
        int nuevoId = IdGenerator.obtenerNuevoId("vehiculo", 2000);
        
        // 3. Seteamos el ID generado en el DTO
        dto.setId(nuevoId);
        
        // 4. Convertir DTO a Modelo (el mapper ahora obtiene el ID y socioId del DTO)
        Vehiculo vehiculo = VehiculoMapper.toModel(dto);
        
        // 5. Guardar
        vehiculoDAO.guardar(vehiculo);
    }

    /**
     * Busca un vehículo por su ID y retorna el DTO.
     * @param id El ID del vehículo a buscar.
     * @return El VehiculoDTO correspondiente.
     * @throws RegistroNoEncontradoException Si no se encuentra el vehículo.
     */
    public VehiculoDTO buscarPorId(int id) throws RegistroNoEncontradoException {
        Vehiculo v = vehiculoDAO.buscarPorId(id);
        if (v == null) {
            throw new RegistroNoEncontradoException("No se encontró vehículo con ID: " + id);
        }
        return VehiculoMapper.toDto(v);
    }

    /**
     * Busca un vehículo por matrícula y retorna el DTO.
     * @param matricula La matrícula a buscar.
     * @return El VehiculoDTO correspondiente.
     * @throws exceptions.RegistroNoEncontradoException Si no se encuentra.
     */
    public VehiculoDTO buscarPorMatricula(String matricula) throws RegistroNoEncontradoException {
        Vehiculo v = vehiculoDAO.buscarPorMatricula(matricula);
        if (v == null) {
            throw new RegistroNoEncontradoException("No se encontró vehículo con matrícula: " + matricula);
        }
        return VehiculoMapper.toDto(v);
    }

    /**
     * Lista todos los vehículos convirtiéndolos a DTOs.
     * @return Lista de VehiculoDTO.
     */
    public List<VehiculoDTO> listarTodos() {
        return vehiculoDAO.listarTodos().stream()
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VehiculoDTO> listarPorSocio(int socioId) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getSocioId() == socioId) // Filtramos por ID de socio
                .map(VehiculoMapper::toDto)              // Mapeamos a DTO
                .collect(Collectors.toList());
    }
    
    /**
     * Filtra vehículos por socio y retorna lista de DTOs.
     * @param socioId El ID del socio propietario.
     * @return Lista de VehiculoDTO del socio.
     */
    public List<VehiculoDTO> buscarVehiculosPorSocio(int socioId) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getSocioId() == socioId)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Filtra vehículos por TipoVehiculo.
     * @param tipo El enum TipoVehiculo a filtrar.
     * @return Lista de VehiculoDTO filtrada.
     */
    public List<VehiculoDTO> buscarPorTipo(TipoVehiculo tipo) {
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getTipo() == tipo)
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * --- CORRECCIÓN/IMPLEMENTACIÓN: AÑADIDO MÉTODO FALTANTE ---
     * Lista los vehículos bajo la responsabilidad de un empleado específico.
     * @param empleadoId El ID del empleado responsable.
     * @return Una lista de VehiculoDTO.
     */
    public List<VehiculoDTO> listarVehiculosPorResponsable(int empleadoId) {
        // Usamos el DAO para obtener todos los vehículos y filtramos por el nuevo campo empleadoId
        return vehiculoDAO.listarTodos().stream()
                .filter(v -> v.getEmpleadoId() == empleadoId) // <--- USAMOS EL CAMPO NUEVO
                .map(VehiculoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un vehículo existente.
     * @param dto El DTO con los datos actualizados (debe contener el ID correcto).
     * @throws RegistroNoEncontradoException Si el vehículo no existe en BD.
     */
    public void actualizarVehiculo(VehiculoDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        if (vehiculoDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No existe vehículo con ID " + dto.getId());
        }

        // Validación de matrícula duplicada (excluyendo el propio ID actual)
        Vehiculo existentePorMatricula = vehiculoDAO.buscarPorMatricula(dto.getMatricula());
        if (existentePorMatricula != null && existentePorMatricula.getId() != dto.getId()) {
               throw new ErrorNegocio("Error: Ya existe otro vehículo con la matrícula: " + dto.getMatricula());
        }
        
        // Mapeo para actualizar (el mapper toma el ID del DTO)
        Vehiculo vehiculo = VehiculoMapper.toModel(dto);
        vehiculoDAO.actualizar(vehiculo);
    }

    /**
     * Elimina un vehículo por su matrícula.
     * @param matricula La matrícula del vehículo a eliminar.
     * @throws RegistroNoEncontradoException Si el vehículo no existe.
     */
    public void eliminarVehiculo(String matricula) throws RegistroNoEncontradoException {
        if (vehiculoDAO.buscarPorMatricula(matricula) == null) {
            throw new RegistroNoEncontradoException("No existe vehículo con matrícula " + matricula);
        }
        vehiculoDAO.eliminar(matricula);
    }
}