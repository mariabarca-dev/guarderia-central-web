package mapper;

import dto.EmpleadoDTO;
import model.Empleado;

public class EmpleadoMapper {

    /**
     * Convierte un EmpleadoDTO a modelo Empleado.
     * Se asume que el DTO ya tiene asignado el ID y el Rol correctamente
     * desde la capa de Servicio.
     */
    public static Empleado toModel(EmpleadoDTO dto) {
        if (dto == null) return null;

        return new Empleado(
                dto.getId(),
                dto.getNombre(),
                dto.getApellido(), // <--- Mapeo de apellido
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getNombreUsuario(),
                dto.getClave(),
                dto.getRol(),       // El Rol ya fue asignado en el Service
                dto.getCodigo(),
                dto.getEspecialidad()
        );
    }

    /**
     * Convierte un modelo Empleado a EmpleadoDTO.
     */
    public static EmpleadoDTO toDto(Empleado model) {
        if (model == null) return null;

        return new EmpleadoDTO(
                model.getId(),
                model.getNombre(),
                model.getApellido(), // <--- Mapeo de apellido
                model.getDireccion(),
                model.getTelefono(),
                model.getNombreUsuario(),
                model.getClave(),
                model.getRol(),
                model.getCodigo(),
                model.getEspecialidad()
        );
    }
}