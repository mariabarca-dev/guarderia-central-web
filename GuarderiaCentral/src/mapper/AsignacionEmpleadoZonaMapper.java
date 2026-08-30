package mapper;

import model.AsignacionEmpleadoZona;
import dto.AsignacionEmpleadoZonaDTO;
// Asumimos que estos mappers existen y están actualizados para recibir DTOs.
import mapper.EmpleadoMapper;
import mapper.ZonaMapper;

public class AsignacionEmpleadoZonaMapper {

    /**
     * Convierte un DTO a Modelo.
     * DELEGACIÓN: Utiliza los mappers de Empleado y Zona para construir los objetos internos.
     * Asume que los DTOs internos ya contienen los IDs correctos seteados por el Service.
     */
    public static AsignacionEmpleadoZona toModel(AsignacionEmpleadoZonaDTO dto) {
        if (dto == null) return null;

        // CORREGIDO: Ahora delegamos la conversión al mapper base de cada entidad.
        // Ya no se pasan IDs externos aquí. El mapeo de relaciones (Zona/Empleado)
        // debe ocurrir en su servicio correspondiente antes de llamar a este mapper.
        return new AsignacionEmpleadoZona(
            EmpleadoMapper.toModel(dto.getEmpleado()),
            ZonaMapper.toModel(dto.getZona()),
            dto.getCantVehiculosACargo()
        );
    }

    /**
     * Convierte un Modelo a DTO.
     */
    public static AsignacionEmpleadoZonaDTO toDto(AsignacionEmpleadoZona model) {
        if (model == null) return null;

        // CORREGIDO: Usamos los mappers de conversión inversa.
        return new AsignacionEmpleadoZonaDTO(
            EmpleadoMapper.toDto(model.getEmpleado()),
            ZonaMapper.toDto(model.getZona()),
            model.getCantVehiculosACargo()
        );
    }
}