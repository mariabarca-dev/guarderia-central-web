package service;

import model.Zona;
import dto.ZonaDTO;
import mapper.ZonaMapper;
import dao.ZonaDAO;
import dao.impl.ZonaDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

/**
 * Servicio para gestionar la lógica de negocio de las zonas.
 */
public class ZonaService {
    
    private ZonaDAO zonaDAO;

    public ZonaService() {
        this.zonaDAO = new ZonaDAOImpl();
    }

    /**
     * Registra una nueva zona.
     * Se encarga de generar el ID y setearlo en el DTO antes de convertir a modelo.
     */
    public void registrarZona(ZonaDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("Error: La zona no puede ser nula.");
        }
        
        // 1. Validación de Regla de Negocio
        if (zonaDAO.buscarPorLetra(dto.getLetra()) != null) {
            throw new ErrorNegocio("Error: Ya existe una zona registrada con la letra: " + dto.getLetra());
        }
        
        // 2. Generar ID para la nueva entidad
        int nuevoId = IdGenerator.obtenerNuevoId("zona", 3000);
        
        // 3. Seteamos el ID generado en el DTO
        dto.setId(nuevoId);
        
        // 4. Convertir DTO a Modelo (el mapper ahora obtiene el ID del DTO)
        Zona zona = ZonaMapper.toModel(dto);
        
        // 5. Persistir
        zonaDAO.guardar(zona);
    }

    /**
     * Busca una zona y retorna el DTO.
     */
    public ZonaDTO buscarPorLetra(String letra) throws RegistroNoEncontradoException {
        Zona z = zonaDAO.buscarPorLetra(letra);
        if (z == null) {
            throw new RegistroNoEncontradoException("No se encontró la zona con letra: " + letra);
        }
        return ZonaMapper.toDto(z);
    }

    /**
     * Lista todas las zonas.
     */
    public List<ZonaDTO> listarTodas() {
        return zonaDAO.listarTodos().stream()
                .map(ZonaMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una zona existente.
     * Se asegura de recuperar el ID existente y setearlo en el DTO para la conversión.
     */
    public void actualizarZona(ZonaDTO dto) throws RegistroNoEncontradoException {
        // 1. Obtenemos el ID existente para asegurar la persistencia
        Zona existente = zonaDAO.buscarPorLetra(dto.getLetra());
        if (existente == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: La zona " + dto.getLetra() + " no existe.");
        }
        
        // 2. Seteamos el ID correcto en el DTO (¡CRUCIAL!)
        dto.setId(existente.getId());
        
        // 3. Convertimos DTO a Modelo (el mapper usa el ID del DTO)
        Zona zonaActualizada = ZonaMapper.toModel(dto);
        
        // 4. Actualizar
        zonaDAO.actualizar(zonaActualizada);
    }

    public void eliminarZona(String letra) throws RegistroNoEncontradoException {
        if (zonaDAO.buscarPorLetra(letra) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: La zona " + letra + " no existe.");
        }
        zonaDAO.eliminar(letra);
    }
}