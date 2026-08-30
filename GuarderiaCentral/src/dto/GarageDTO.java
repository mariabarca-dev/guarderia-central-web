package dto;

import java.time.LocalDate;

public class GarageDTO {

    private int id;
    private int numeroGarage;
    private float lecturaLuz;
    private boolean servicioMantenimiento;
    private String socioPropietario;
    private LocalDate fechaCompra;
    private String zona;

    public GarageDTO(int id, int numeroGarage, float lecturaLuz, boolean servicioMantenimiento, String socioPropietario, LocalDate fechaCompra, String zona) {
        this.id = id;
        this.numeroGarage = numeroGarage;
        this.lecturaLuz = lecturaLuz;
        this.servicioMantenimiento = servicioMantenimiento;
        this.socioPropietario = socioPropietario;
        this.fechaCompra = fechaCompra;
        this.zona = zona; // <--- Asignación del nuevo atributo
    }

    public int getId() {
        return id;
    }

    public int getNumeroGarage() {
        return numeroGarage;
    }

    public float getLecturaLuz() {
        return lecturaLuz;
    }

    public boolean isServicioMantenimiento() {
        return servicioMantenimiento;
    }

    public String getSocioPropietario() {
        return socioPropietario;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public String getZona() {
        return zona;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumeroGarage(int numeroGarage) {
        this.numeroGarage = numeroGarage;
    }

    public void setLecturaLuz(float lecturaLuz) {
        this.lecturaLuz = lecturaLuz;
    }

    public void setServicioMantenimiento(boolean servicioMantenimiento) {
        this.servicioMantenimiento = servicioMantenimiento;
    }

    public void setSocioPropietario(String socioPropietario) {
        this.socioPropietario = socioPropietario;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

}
