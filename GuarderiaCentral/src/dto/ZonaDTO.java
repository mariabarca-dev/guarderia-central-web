package dto;

public class ZonaDTO {
    
    private int id;
    private String letra;
    private String tipoVehiculo; // <--- CORREGIDO: Sin acento
    private int capacidadVehiculos;
    private float ancho;
    private float largo;

    public ZonaDTO(int id, String letra, String tipoVehiculo, int capacidadVehiculos, float ancho,
            float largo) {
        this.id = id;
        this.letra = letra;
        this.tipoVehiculo = tipoVehiculo; // <--- CORREGIDO: Sin acento
        this.capacidadVehiculos = capacidadVehiculos;
        this.ancho = ancho;
        this.largo = largo;
    }

    public int getId() {
        return id;
    }

    public String getLetra() {
        return letra;
    }

    // <--- CORREGIDO: Getter sin acento
    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public int getCapacidadVehiculos() {
        return capacidadVehiculos;
    }

    public float getAncho() {
        return ancho;
    }

    public float getLargo() {
        return largo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    // <--- CORREGIDO: Setter sin acento
    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public void setCapacidadVehiculos(int capacidadVehiculos) {
        this.capacidadVehiculos = capacidadVehiculos;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }
}