package mapper;

import model.Socio;
import model.Rol;
import dto.SocioDTO;

public class SocioMapper {

    /**
     * Convierte un SocioDTO a un modelo Socio.
     * Asume que el ID ya ha sido asignado al DTO por la capa de Servicio.
     * @param dto El objeto SocioDTO origen.
     * @return Un nuevo objeto Socio con los datos del DTO.
     */
    public static Socio toModel(SocioDTO dto) { // <--- Firma simplificada (sin el int id externo)
        if (dto == null) return null;

        return new Socio(
            dto.getId(), // <--- Obtenemos el ID directamente del DTO
            dto.getNombre(),
            dto.getDireccion(),
            dto.getTelefono(),
            dto.getNombreUsuario(),
            dto.getClave(),
            Rol.SOCIO, // Rol fijo por lógica de negocio
            dto.getDni(),
            dto.getFechaIngreso()
        );
    }

    /**
     * Versión sobrecargada para casos donde se requiera un ID específico (poco común con DTOs).
     * @deprecated Se prefiere usar toModel(SocioDTO) donde el ID viene en el DTO.
     */
    @Deprecated
    public static Socio toModel(SocioDTO dto, int id) {
        if (dto == null) return null;
        // Forzamos el ID recibido por parámetro (si se usa la sobrecarga)
        return new Socio(
            id,
            dto.getNombre(),
            dto.getDireccion(),
            dto.getTelefono(),
            dto.getNombreUsuario(),
            dto.getClave(),
            Rol.SOCIO,
            dto.getDni(),
            dto.getFechaIngreso()
        );
    }

    /**
     * Convierte un modelo Socio a un SocioDTO.
     * @param model El objeto Socio modelo origen.
     * @return Un nuevo SocioDTO con los datos del modelo.
     */
    public static SocioDTO toDto(Socio model) {
        if (model == null) return null;

        return new SocioDTO(
            model.getId(),
            model.getNombre(),
            model.getDireccion(),
            model.getTelefono(),
            model.getNombreUsuario(),
            model.getClave(),
            model.getRol(), // Usamos el rol del modelo
            model.getDni(),
            model.getFechaIngreso()
        );
    }
}