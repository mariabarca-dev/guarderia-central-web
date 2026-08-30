package service;

import model.Empleado;
import dto.EmpleadoDTO;
import mapper.EmpleadoMapper;
import dao.EmpleadoDAO;
import dao.impl.EmpleadoDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

/**
 * Servicio que gestiona la lógica de negocio de los Empleados.
 * Actúa como intermediario entre el controlador y el DAO,
 * manejando la conversión entre Modelos y DTOs.
 */
public class EmpleadoService {

    private EmpleadoDAO empleadoDAO;

    public EmpleadoService() {
        // Inyección de dependencia directa (podría mejorarse con un framework)
        this.empleadoDAO = new EmpleadoDAOImpl();
    }

    /**
     * Registra un nuevo empleado en el sistema.
     * Se encarga de validar la unicidad del código, generar el ID y persistir el dato.
     *
     * @param dto Objeto de transferencia con los datos del nuevo empleado.
     * @throws ErrorNegocio Si el DTO es nulo o el código ya existe.
     */
    public void registrarEmpleado(EmpleadoDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El objeto empleado no puede ser nulo.");
        }

        // Regla de Negocio: Validación de Código único
        // Se asume que el DAO implementa este método para buscar en la base de datos/archivo.
        if (empleadoDAO.buscarPorCodigo(dto.getCodigo()) != null) {
            throw new ErrorNegocio("Error: Ya existe un empleado registrado con el código: " + dto.getCodigo());
        }

        // Generar ID único para la nueva entidad.
        // Se utiliza "empleado" como prefijo para el archivo de texto contador.
        int nuevoId = IdGenerator.obtenerNuevoId("empleado", 300);

        // Asignamos el ID generado al DTO.
        dto.setId(nuevoId);

        // Mapeo: Convertimos el DTO a Modelo para interactuar con la capa de persistencia.
        Empleado empleadoModelo = EmpleadoMapper.toModel(dto);

        // Persistimos el modelo.
        empleadoDAO.guardar(empleadoModelo);
    }

    /**
     * Obtiene una lista completa de todos los empleados registrados.
     *
     * @return Lista de EmpleadoDTO.
     */
    public List<EmpleadoDTO> listarTodos() {
        // Obtenemos la lista de modelos del DAO y la transformamos a una lista de DTOs.
        return empleadoDAO.listarTodos().stream()
                .map(EmpleadoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un empleado existente.
     *
     * @param dto DTO con los datos actualizados (debe contener el ID correcto).
     * @throws RegistroNoEncontradoException Si el empleado no existe en la base de datos.
     */
    public void actualizarEmpleado(EmpleadoDTO dto) throws RegistroNoEncontradoException {
        // Verificamos que el empleado exista antes de intentar actualizar.
        if (empleadoDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: No se encontró un empleado con ID " + dto.getId());
        }
        
        // Convertimos el DTO actualizado a modelo y lo pasamos al DAO.
        empleadoDAO.actualizar(EmpleadoMapper.toModel(dto));
    }

    /**
     * Elimina un empleado del sistema de forma permanente.
     *
     * @param id El ID del empleado a eliminar.
     * @throws RegistroNoEncontradoException Si el empleado no existe en la base de datos.
     */
    public void eliminarEmpleado(int id) throws RegistroNoEncontradoException {
        // Verificamos que el empleado exista antes de intentar eliminar.
        if (empleadoDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: No se encontró un empleado con ID " + id);
        }
        
        // Ejecutamos la eliminación en el DAO.
        empleadoDAO.eliminar(id);
    }
}