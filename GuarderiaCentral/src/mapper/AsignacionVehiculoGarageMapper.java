package mapper;

import model.AsignacionVehiculoGarage;
import dto.AsignacionVehiculoGarageDTO;
// Importamos los mappers de los objetos que componen esta asignación
import model.Garage;
import model.Vehiculo;


public class AsignacionVehiculoGarageMapper {

    /**
     * Convierte un AsignacionVehiculoGarageDTO a su Modelo correspondiente.
     * Utiliza los mappers de Vehiculo y Garage para la conversión anidada.
     * Asume que la capa de Servicio ya ha asignado los IDs correctos a los DTOs anidados.
     * @param dto El objeto DTO origen.
     * @return Un nuevo objeto AsignacionVehiculoGarage con los datos del DTO.
     */
    public static AsignacionVehiculoGarage toModel(AsignacionVehiculoGarageDTO dto, Garage garage, Vehiculo vehiculo) {
        if (dto == null) {
            return null;
        }

        // CORRECTO: Ambos métodos reciben solo el DTO.
        // El mapeo de relaciones (como Socio/Zona en Garage) debe ocurrir en el GarageService
        // ANTES de llamar a este mapper global.
        return new AsignacionVehiculoGarage(
            vehiculo,
            garage,
            dto.getFechaAsignacionGarage()
        );
    }
    
  
   
    

    /**
     * Convierte un Modelo AsignacionVehiculoGarage a su DTO correspondiente.
     * Utiliza los mappers de Vehiculo y Garage para la conversión anidada.
     * @param model El objeto Modelo origen.
     * @return Un nuevo AsignacionVehiculoGarageDTO con los datos del modelo.
     */
    public static AsignacionVehiculoGarageDTO toDto(AsignacionVehiculoGarage model) {
        if (model == null) {
            return null;
        }

        return new AsignacionVehiculoGarageDTO(
            VehiculoMapper.toDto(model.getVehiculo()),
            GarageMapper.toDto(model.getGarage()),
            model.getFechaAsignacionGarage()
        );
    }
}