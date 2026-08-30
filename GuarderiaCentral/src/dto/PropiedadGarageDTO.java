
package dto;

import java.time.LocalDate;


public class PropiedadGarageDTO {
    
    private SocioDTO socio;
    private GarageDTO garage;
    private LocalDate fechaCompraGarage;

    public PropiedadGarageDTO(SocioDTO socio, GarageDTO garage, LocalDate fechaCompraGarage) {
        this.socio = socio;
        this.garage = garage;
        this.fechaCompraGarage = fechaCompraGarage;
    }

    public SocioDTO getSocio() {
        return socio;
    }

    public GarageDTO getGarage() {
        return garage;
    }

    public LocalDate getFechaCompraGarage() {
        return fechaCompraGarage;
    }

    public void setSocioDTO(SocioDTO socio) {
        this.socio = socio;
    }

    public void setGarageDTO(GarageDTO garage) {
        this.garage = garage;
    }

    public void setFechaCompraGarage(LocalDate fechaCompraGarage) {
        this.fechaCompraGarage = fechaCompraGarage;
    }


    
}
