package mapper;

import model.Administrador;
import model.Rol;
import dto.AdministradorDTO;

public class AdministradorMapper {

    /**
     * Convierte un AdministradorDTO a un modelo Administrador.
     * Asume que el ID ya ha sido asignado al DTO por la capa de Servicio.
     * @param dto El objeto AdministradorDTO origen.
     * @return Un nuevo objeto Administrador con los datos del DTO.
     */
    public static Administrador toModel(AdministradorDTO dto) { // <--- Firma simplificada (sin el int id externo)
        if (dto == null) return null;

        return new Administrador(
            dto.getId(), // <--- Obtenemos el ID directamente del DTO
            dto.getNombre(),
            dto.getDireccion(),
            dto.getTelefono(),
            dto.getNombreUsuario(),
            dto.getClave(),
            Rol.ADMINISTRADOR // Rol fijo por seguridad
        );
    }

    /**
     * Versión sobrecargada para retrocompatibilidad o casos excepcionales.
     * @deprecated Se prefiere usar toModel(AdministradorDTO).
     */
    @Deprecated
    public static Administrador toModel(AdministradorDTO dto, int id) {
        if (dto == null) return null;
        // Si se usa este método, fuerza el ID pasado por parámetro
        return new Administrador(
            id,
            dto.getNombre(),
            dto.getDireccion(),
            dto.getTelefono(),
            dto.getNombreUsuario(),
            dto.getClave(),
            Rol.ADMINISTRADOR
        );
    }

    /**
     * Convierte un modelo Administrador a un AdministradorDTO.
     * @param model El objeto Administrador modelo origen.
     * @return Un nuevo AdministradorDTO con los datos del modelo.
     */
    public static AdministradorDTO toDto(Administrador model) {
        if (model == null) return null;

        return new AdministradorDTO(
            model.getId(),
            model.getNombre(),
            model.getDireccion(),
            model.getTelefono(),
            model.getNombreUsuario(),
            model.getClave(),
            model.getRol()
        );
    }
}