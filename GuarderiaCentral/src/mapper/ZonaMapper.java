package mapper;

import model.Zona;
import model.TipoVehiculo;
import dto.ZonaDTO;

public class ZonaMapper {

    /**
     * Convierte un ZonaDTO a un modelo Zona.
     * Asume que el ID ya ha sido asignado al DTO por la capa de Servicio.
     */
    public static Zona toModel(ZonaDTO dto) {
        if (dto == null) return null;

        return new Zona(
            dto.getId(),
            dto.getLetra(),
            // --- CORREGIDO AQUÍ: Quitamos los acentos para que coincida con ZonaDTO ---
            // Ahora es getTipoVehiculo() y getCapacidadVehiculos()
            TipoVehiculo.valueOf(dto.getTipoVehiculo().toUpperCase()),
            dto.getCapacidadVehiculos(),
            dto.getAncho(),
            dto.getLargo()
        );
    }

    /**
     * Convierte un modelo Zona a un ZonaDTO.
     */
    public static ZonaDTO toDto(Zona model) {
        if (model == null) return null;

        return new ZonaDTO(
            model.getId(),
            model.getLetra(),
            // Conversión de Enum (Modelo) a String (DTO)
            model.getTipoVehiculo().name(), 
            model.getCapacidadVehiculos(),
            // --- CORREGIDO AQUÍ: Aseguramos consistencia con los getters del Modelo ---
            // Asumo que en tu modelo los getters son getAnchoGarage() y getLargoGarage()
            // tal como los pusiste en el ejemplo original.
            model.getAnchoGarage(), 
            model.getLargoGarage()  
        );
    }
}