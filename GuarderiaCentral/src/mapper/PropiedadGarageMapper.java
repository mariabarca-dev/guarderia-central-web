package mapper;

import model.PropiedadGarage;
import model.Socio;
import model.Garage;
import dto.PropiedadGarageDTO;

public class PropiedadGarageMapper {

    /**
     * Convierte los objetos ya construidos (Socio y Garage) y el DTO a un modelo PropiedadGarage.
     */
    public static PropiedadGarage toModel(PropiedadGarageDTO dto, Socio socio, Garage garage) {
        if (dto == null) return null;

        return new PropiedadGarage(
            socio, // Pasamos el modelo ya buscado y validado
            garage, // Pasamos el modelo ya buscado y validado
            dto.getFechaCompraGarage()
        );
    }

    /**
     * Convierte un modelo PropiedadGarage a un PropiedadGarageDTO.
     */
    public static PropiedadGarageDTO toDto(PropiedadGarage model) {
        if (model == null) return null;

        return new PropiedadGarageDTO(
            SocioMapper.toDto(model.getSocio()),   // Usamos el mapper de socio
            GarageMapper.toDto(model.getGarage()), // Usamos el mapper de garage
            model.getFechaCompraGarage()
        );
    }
}