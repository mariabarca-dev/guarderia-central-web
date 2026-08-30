package mapper;

import model.Usuario;
import model.Socio;
import model.Empleado;
import model.Administrador; // Importación necesaria
import dto.UsuarioDTO;
import dto.SocioDTO;
import dto.EmpleadoDTO;
import dto.AdministradorDTO; // Importación necesaria

public class UsuarioMapper {

    /**
     * Convierte un modelo Usuario a un UsuarioDTO (Mantiene tu lógica
     * original).
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

        // Fallback genérico si fuera solo Usuario (abstracto, quizás no ocurra)
        return new UsuarioDTO(
                model.getId(), model.getNombre(), model.getDireccion(),
                model.getTelefono(), model.getNombreUsuario(), model.getClave(),
                model.getRol()
        );
    }

    /**
     * NUEVO MÉTODO: Convierte un UsuarioDTO a un modelo Usuario concreto.
     * Utiliza los mappers específicos de cada subclase para no perder datos.
     */
    public static Usuario toModel(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        // 1. Si es una instancia específica, úsala
        if (dto instanceof AdministradorDTO) {
            return AdministradorMapper.toModel((AdministradorDTO) dto);
        }
        if (dto instanceof EmpleadoDTO) {
            return EmpleadoMapper.toModel((EmpleadoDTO) dto);
        }
        if (dto instanceof SocioDTO) {
            return SocioMapper.toModel((SocioDTO) dto);
        }

        // 2. SI LLEGA UN UsuarioDTO GENÉRICO, USAMOS EL ROL PARA CREAR EL MODELO
        // (Esto soluciona el error en el LoginController)
        switch (dto.getRol()) {
            case ADMINISTRADOR:
                return new Administrador(dto.getId(), dto.getNombre(), dto.getDireccion(),dto.getTelefono(), dto.getNombreUsuario(), dto.getClave(), dto.getRol());
            case EMPLEADO:
                // Ponemos valores por defecto si no tenemos los específicos en el DTO genérico
                return new Empleado(dto.getId(), dto.getNombre(), dto.getDireccion(),
                        dto.getTelefono(), dto.getNombreUsuario(), dto.getClave(),
                        dto.getRol(), "N/A", "N/A");
            case SOCIO:
                return new Socio(dto.getId(), dto.getNombre(), dto.getDireccion(),
                        dto.getTelefono(), dto.getNombreUsuario(), dto.getClave(),
                        dto.getRol(), "N/A", java.time.LocalDate.now());
            default:
                throw new IllegalArgumentException("Rol no reconocido: " + dto.getRol());
        }
    }
}
