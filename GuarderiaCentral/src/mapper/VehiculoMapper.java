package mapper;

import model.Vehiculo;
import model.TipoVehiculo;
import dto.VehiculoDTO;

public class VehiculoMapper {

    public static Vehiculo toModel(VehiculoDTO dto) {
        if (dto == null) return null;

        return new Vehiculo(
                dto.getId(),
                dto.getSocioId(),
                dto.getEmpleadoId(),
                dto.getNombre(),
                dto.getMatricula(),
                TipoVehiculo.valueOf(dto.getTipo().toUpperCase()),
                dto.getProfundidad(),
                dto.getAncho()
        );
    }

    public static VehiculoDTO toDto(Vehiculo model) {
        if (model == null) return null;

        return new VehiculoDTO(
                model.getId(),
                model.getSocioId(),
                model.getEmpleadoId(),
                model.getNombre(),
                model.getMatricula(),
                model.getTipo().name(),
                model.getProfundidad(),
                model.getAncho()
        );
    }
}