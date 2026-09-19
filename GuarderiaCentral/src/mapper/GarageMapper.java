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

        // CORRECCIÓN: Si no hay socio propietario, devolvemos null en lugar de la palabra "Libre"
        // para que las validaciones lógicas del sistema (como if (socioPropietario != null)) funcionen bien.
        String socioPropietarioStr = (model.getSocioPropietario() != null) ? model.getSocioPropietario().getDni() : null;
        String zonaLetra = (model.getZona() != null) ? model.getZona().getLetra() : "Sin asignar";

        return new GarageDTO(
                model.getId(),
                model.getNumeroGarage(),
                (float) model.getLecturaLuz(),
                model.isServicioMantenimiento(),
                socioPropietarioStr,
                model.getFechaCompra(),
                zonaLetra
        );
    }
}