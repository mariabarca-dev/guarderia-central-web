
package dto;

import java.time.LocalDate;


public class AsignacionVehiculoGarageDTO {
     private VehiculoDTO vehiculo;
    private GarageDTO garage;
    private LocalDate fechaAsignacionGarage;

    public AsignacionVehiculoGarageDTO(VehiculoDTO vehiculo, GarageDTO garage, LocalDate fechaAsignacionGarage) {
        this.vehiculo = vehiculo;
        this.garage = garage;
        this.fechaAsignacionGarage = fechaAsignacionGarage;
    }

    public VehiculoDTO getVehiculo() {
        return vehiculo;
    }

    public GarageDTO getGarage() {
        return garage;
    }

    public LocalDate getFechaAsignacionGarage() {
        return fechaAsignacionGarage;
    }

    public void setVehiculoDTO(VehiculoDTO vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setGarageDTO(GarageDTO garage) {
        this.garage = garage;
    }

    public void setFechaAsignacionGarage(LocalDate fechaAsignacionGarage) {
        this.fechaAsignacionGarage = fechaAsignacionGarage;
    }
    
    

}
