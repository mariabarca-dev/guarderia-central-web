package mapper;

import model.Administrador;
import model.Rol;
import dto.AdministradorDTO;

public class AdministradorMapper {

    /**
     * Convierte un AdministradorDTO a su modelo de dominio Administrador.
     */
    public static Administrador toModel(AdministradorDTO dto) {
        if (dto == null) return null;

        return new Administrador(
                dto.getId(),
                dto.getNombre(),
                dto.getApellido(), // <--- Atributo mapeado
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getNombreUsuario(),
                dto.getClave(),
                Rol.ADMINISTRADOR
        );
    }

    /**
     * Convierte un modelo de dominio Administrador a AdministradorDTO.
     */
    public static AdministradorDTO toDto(Administrador model) {
        if (model == null) return null;

        return new AdministradorDTO(
                model.getId(),
                model.getNombre(),
                model.getApellido(), // <--- Atributo mapeado
                model.getDireccion(),
                model.getTelefono(),
                model.getNombreUsuario(),
                model.getClave(),
                model.getRol()
        );
    }
}