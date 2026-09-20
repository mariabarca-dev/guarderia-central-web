package mapper;

import model.Zona;
import model.TipoVehiculo;
import dto.ZonaDTO;

public class ZonaMapper {

    public static Zona toModel(ZonaDTO dto) {
        if (dto == null) return null;

        return new Zona(
                dto.getId(),
                dto.getLetra(),
                TipoVehiculo.valueOf(dto.getTipoVehiculo().toUpperCase()),
                dto.getCapacidadVehiculos(),
                dto.getAncho(),
                dto.getLargo()
        );
    }

    public static ZonaDTO toDto(Zona model) {
        if (model == null) return null;

        return new ZonaDTO(
                model.getId(),
                model.getLetra(),
                model.getTipoVehiculo().name(),
                model.getCapacidadVehiculos(),
                model.getAnchoGarage(),
                model.getLargoGarage()
        );
    }
}