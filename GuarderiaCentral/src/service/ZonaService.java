package service;

import model.Zona;
import dto.ZonaDTO;
import mapper.ZonaMapper;
import dao.ZonaDAO;
import dao.GarageDAO;
import dao.impl.ZonaDAOImpl;
import dao.impl.GarageDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;

import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

/**
 * Servicio para gestionar la lógica de negocio de las zonas.
 * Contiene únicamente las reglas de negocio e integridad del sistema.
 */
public class ZonaService {

    private final ZonaDAO zonaDAO;
    private final GarageDAO garageDAO;

    public ZonaService() {
        this.zonaDAO = new ZonaDAOImpl();
        this.garageDAO = new GarageDAOImpl();
    }

    /**
     * Registra una nueva zona.
     * Valida la unicidad de la letra e incrementa el identificador.
     */
    public void registrarZona(ZonaDTO dto) throws ErrorNegocio {
        // Normalizar a mayúsculas antes de validar
        dto.setLetra(dto.getLetra().trim().toUpperCase());

        if (zonaDAO.buscarPorLetra(dto.getLetra()) != null) {
            throw new ErrorNegocio("Error: Ya existe una zona registrada con la letra: " + dto.getLetra());
        }

        int nuevoId = IdGenerator.obtenerNuevoId("zona", 3000);
        dto.setId(nuevoId);

        Zona zona = ZonaMapper.toModel(dto);
        zonaDAO.guardar(zona);
    }

    /**
     * Busca una zona por letra y retorna su DTO.
     */
    public ZonaDTO buscarPorLetra(String letra) throws RegistroNoEncontradoException {
        Zona z = zonaDAO.buscarPorLetra(letra != null ? letra.trim().toUpperCase() : null);
        if (z == null) {
            throw new RegistroNoEncontradoException("No se encontró la zona con letra: " + letra);
        }
        return ZonaMapper.toDto(z);
    }

    /**
     * Lista todas las zonas registradas mapeadas a DTO.
     */
    public List<ZonaDTO> listarTodas() {
        return zonaDAO.listarTodos().stream()
                .map(ZonaMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la información de una zona existente.
     */
    public void actualizarZona(ZonaDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        dto.setLetra(dto.getLetra().trim().toUpperCase());

        Zona existente = zonaDAO.buscarPorLetra(dto.getLetra());
        if (existente == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: La zona " + dto.getLetra() + " no existe.");
        }

        if (!dto.getTipoVehiculo().equalsIgnoreCase(existente.getTipoVehiculo().name())) {
            boolean tieneGarajes = garageDAO.listarTodos().stream()
                    .anyMatch(g -> g.getZona() != null && g.getZona().getLetra().equalsIgnoreCase(dto.getLetra()));

            if (tieneGarajes) {
                throw new ErrorNegocio("Error: No se puede cambiar el tipo de vehículo de la zona '"
                        + dto.getLetra() + "' porque ya posee garajes asociados.");
            }
        }

        dto.setId(existente.getId());
        Zona zonaActualizada = ZonaMapper.toModel(dto);
        zonaDAO.actualizar(zonaActualizada);
    }

    /**
     * Elimina una zona del sistema.
     * Garantiza la integridad referencial impidiendo borrar zonas con garajes.
     */
    public void eliminarZona(String letra) throws RegistroNoEncontradoException, ErrorNegocio {
        String letraNorm = letra != null ? letra.trim().toUpperCase() : "";
        Zona zona = zonaDAO.buscarPorLetra(letraNorm);
        if (zona == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: La zona " + letra + " no existe.");
        }

        boolean tieneGarajes = garageDAO.listarTodos().stream()
                .anyMatch(g -> g.getZona() != null && g.getZona().getLetra().equalsIgnoreCase(letraNorm));

        if (tieneGarajes) {
            throw new ErrorNegocio("Error: No se puede eliminar la zona '" + letra
                    + "' porque existen garajes asociados a ella.");
        }

        zonaDAO.eliminar(letraNorm);
    }
}