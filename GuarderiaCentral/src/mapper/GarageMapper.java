
package mapper;

import model.Garage;
import model.Socio;
import model.Zona;
import dto.GarageDTO;

public class GarageMapper {

    /**
     * Convierte un GarageDTO a modelo Garage.
     * Requiere pasar los objetos Socio y Zona previamente buscados en sus DAOs.
     */
    public static Garage toModel(GarageDTO dto, Socio socio, Zona zona) {
        if (dto == null) return null;

        return new Garage(
            dto.getId(),
            dto.getNumeroGarage(),
            (double) dto.getLecturaLuz(), // Conversión float a double
            dto.isServicioMantenimiento(),
            socio,
            dto.getFechaCompra(),
            zona
        );
    }

    /**
     * Convierte un modelo Garage a GarageDTO.
     */
    public static GarageDTO toDto(Garage model) {
        if (model == null) return null;

        // Extraemos información necesaria para los Strings del DTO
        String socioNombre = (model.getSocioPropietario() != null) ? model.getSocioPropietario().getNombre() : "Libre";
        String zonaLetra = (model.getZona() != null) ? model.getZona().getLetra() : "Sin asignar";

        return new GarageDTO(
            model.getId(),
            model.getNumeroGarage(),
            (float) model.getLecturaLuz(),
            model.isServicioMantenimiento(),
            socioNombre,
            model.getFechaCompra(),
            zonaLetra
        );
    }
}