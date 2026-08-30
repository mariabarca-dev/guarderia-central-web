package mapper;

import model.Vehiculo;
import model.TipoVehiculo;
import dto.VehiculoDTO;

public class VehiculoMapper {

    /**
     * Convierte un VehiculoDTO a un modelo Vehiculo.
     * Se encarga de recuperar todos los IDs (id, socioId, empleadoId) directamente del DTO.
     * Realiza la conversión del String de tipo a Enum TipoVehiculo.
     * @param dto El objeto VehiculoDTO origen.
     * @return Un nuevo objeto Vehiculo con los datos del DTO.
     */
    public static Vehiculo toModel(VehiculoDTO dto) {
        if (dto == null) return null;

        return new Vehiculo(
            dto.getId(),          // Obtenemos el ID principal del DTO
            dto.getSocioId(),     // Obtenemos el socioId del DTO
            dto.getEmpleadoId(),  // <--- CAMBIO 1: Obtenemos el empleadoId del DTO
            dto.getNombre(),
            dto.getMatricula(),
            // Conversión segura String -> Enum (usando .toUpperCase() para evitar errores de formato)
            TipoVehiculo.valueOf(dto.getTipo().toUpperCase()), 
            dto.getProfundidad(),
            dto.getAncho()
        );
    }

    /**
     * Convierte un modelo Vehiculo a un VehiculoDTO.
     * Se encarga de transferir todos los IDs (id, socioId, empleadoId) al DTO.
     * @param model El objeto Vehiculo modelo origen.
     * @return Un nuevo VehiculoDTO con los datos del modelo.
     */
    public static VehiculoDTO toDto(Vehiculo model) {
        if (model == null) return null;

        return new VehiculoDTO(
            model.getId(),
            model.getSocioId(),
            model.getEmpleadoId(), // <--- CAMBIO 2: Transferimos el empleadoId al DTO
            model.getNombre(),
            model.getMatricula(),
            model.getTipo().name(), // Conversión Enum -> String
            model.getProfundidad(),
            model.getAncho()
        );
    }
}