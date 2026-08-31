package mapper;

import model.Usuario;
import model.Socio;
import model.Empleado;
import model.Administrador;
import dto.UsuarioDTO;
import dto.SocioDTO;
import dto.EmpleadoDTO;
import dto.AdministradorDTO;

public class UsuarioMapper {

    /**
     * Convierte un modelo Usuario a un UsuarioDTO.
     */
    public static UsuarioDTO toDto(Usuario model) {
        if (model == null) {
            return null;
        }

        if (model instanceof Empleado) {
            return EmpleadoMapper.toDto((Empleado) model);
        }
        if (model instanceof Socio) {
            return SocioMapper.toDto((Socio) model);
        }

        // Fallback genérico si fuera solo Usuario
        return new UsuarioDTO(
                model.getId(), model.getNombre(), model.getDireccion(),
                model.getTelefono(), model.getNombreUsuario(), model.getClave(),
                model.getRol()
        );
    }

    /**
     * Convierte un UsuarioDTO a un modelo Usuario concreto.
     */
    public static Usuario toModel(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        // 1. Si es una instancia específica, se usan los mappers correspondientes
        if (dto instanceof AdministradorDTO) {
            return AdministradorMapper.toModel((AdministradorDTO) dto);
        }
        if (dto instanceof EmpleadoDTO) {
            return EmpleadoMapper.toModel((EmpleadoDTO) dto);
        }
        if (dto instanceof SocioDTO) {
            return SocioMapper.toModel((SocioDTO) dto);
        }

        // 2. Si llega un UsuarioDTO genérico, se evalúa el ROL para instanciar el Modelo
        switch (dto.getRol()) {
            case SUPERADMINISTRADOR:
            case ADMINISTRADOR:
                return new Administrador(
                        dto.getId(),
                        dto.getNombre(),
                        dto.getDireccion(),
                        dto.getTelefono(),
                        dto.getNombreUsuario(),
                        dto.getClave(),
                        dto.getRol()
                );

            case EMPLEADO:
                return new Empleado(
                        dto.getId(),
                        dto.getNombre(),
                        dto.getDireccion(),
                        dto.getTelefono(),
                        dto.getNombreUsuario(),
                        dto.getClave(),
                        dto.getRol(),
                        "N/A",
                        "N/A"
                );

            case SOCIO:
                return new Socio(
                        dto.getId(),
                        dto.getNombre(),
                        dto.getDireccion(),
                        dto.getTelefono(),
                        dto.getNombreUsuario(),
                        dto.getClave(),
                        dto.getRol(),
                        "N/A",
                        java.time.LocalDate.now()
                );

            default:
                throw new IllegalArgumentException("Rol no reconocido: " + dto.getRol());
        }
    }
}