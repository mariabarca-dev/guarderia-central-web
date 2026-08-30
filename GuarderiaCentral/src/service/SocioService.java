package service;

import model.Socio;
import dto.SocioDTO;
import mapper.SocioMapper;
import dao.SocioDAO;
import dao.impl.SocioDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.DniDuplicadoException;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

/**
 * Servicio para gestionar la lógica de negocio de los Socios.
 */
public class SocioService {

    private SocioDAO socioDAO;

    public SocioService() {
        this.socioDAO = new SocioDAOImpl();
    }

    /**
     * Registra un nuevo socio.
     * Se encarga de generar el ID y setearlo en el DTO antes de convertir a modelo.
     * @param dto El DTO con los datos del nuevo socio.
     * @throws ErrorNegocio Si hay error de negocio o DNI duplicado.
     */
    public void registrarSocio(SocioDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El socio (datos) no puede ser nulo.");
        }

        // 1. Validaciones de negocio
        if (dto.getFechaIngreso() != null && dto.getFechaIngreso().isAfter(java.time.LocalDate.now())) {
            throw new ErrorNegocio("Error: La fecha de ingreso no puede ser una fecha futura.");
        }

        if (socioDAO.buscarPorDni(dto.getDni()) != null) {
            throw new DniDuplicadoException("Error: Ya existe un socio con el DNI: " + dto.getDni());
        }

        // 2. Generación de ID para la nueva entidad
        int nuevoId = IdGenerator.obtenerNuevoId("socio", 600);
        
        // 3. Seteamos el ID generado en el DTO para que el mapper lo use
        dto.setId(nuevoId);
        
        // 4. Convertir DTO a Modelo (el mapper toma el ID del DTO)
        Socio socio = SocioMapper.toModel(dto);

        // 5. Persistencia
        socioDAO.guardar(socio);
    }

    /**
     * Busca un socio por ID y lo devuelve como DTO.
     * @param id El ID del socio a buscar.
     * @return El SocioDTO correspondiente.
     * @throws RegistroNoEncontradoException Si no se encuentra el socio.
     */
    public SocioDTO buscarPorId(int id) throws RegistroNoEncontradoException {
        Socio s = socioDAO.buscarPorId(id);
        if (s == null) {
            throw new RegistroNoEncontradoException("No se encontró el socio con ID: " + id);
        }
        return SocioMapper.toDto(s);
    }

    /**
     * Lista todos los socios convirtiéndolos a una lista de DTOs.
     * @return Lista de SocioDTO.
     */
    public List<SocioDTO> listarTodos() {
        return socioDAO.listarTodos().stream()
                .map(SocioMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un socio existente.
     * @param dto El DTO con los datos actualizados (debe contener el ID correcto).
     * @throws RegistroNoEncontradoException Si el socio no existe.
     */
    public void actualizarSocio(SocioDTO dto) throws RegistroNoEncontradoException {
        // 1. Verificamos que el socio existe antes de intentar actualizar
        if (socioDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: Socio no encontrado con ID " + dto.getId());
        }
        
        // 2. Convertimos el DTO a modelo para actualizar (el mapper usa el ID del DTO)
        Socio socioActualizado = SocioMapper.toModel(dto);
        
        // 3. Actualizar
        socioDAO.actualizar(socioActualizado);
    }

    /**
     * Elimina un socio por su ID.
     * @param id El ID del socio a eliminar.
     * @throws RegistroNoEncontradoException Si el socio no existe.
     */
    public void eliminarSocio(int id) throws RegistroNoEncontradoException {
        if (socioDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: Socio no encontrado con ID " + id);
        }
        socioDAO.eliminar(id);
    }
}