package mapper;

import model.Administrador;
import model.Rol;
import dto.AdministradorDTO;

public class AdministradorMapper {

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
     * @deprecated Se recomienda asignar el ID al DTO y utilizar {@link #toModel(AdministradorDTO)}.
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