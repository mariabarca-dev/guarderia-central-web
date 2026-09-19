package dto;

public class ZonaDTO {

    private int id;
    private String letra;
    private String tipoVehiculo;
    private int capacidadVehiculos;
    private float ancho;
    private float largo;

    /**
     * Constructor vacío requerido para instanciación mediante setters.
     */
    public ZonaDTO() {
    }

    /**
     * Constructor completo con todos los atributos.
     */
    public ZonaDTO(int id, String letra, String tipoVehiculo, int capacidadVehiculos, float ancho, float largo) {
        this.id = id;
        this.letra = letra;
        this.tipoVehiculo = tipoVehiculo;
        this.capacidadVehiculos = capacidadVehiculos;
        this.ancho = ancho;
        this.largo = largo;
    }

    // --- GETTERS ---
    public int getId() {
        return id;
    }

    public String getLetra() {
        return letra;
    }

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

    // --- ALIAS DE COMPATIBILIDAD CON LA VISTA ---
    public int getCapacidad() {
        return capacidadVehiculos;
    }

    public void setCapacidad(int capacidad) {
        this.capacidadVehiculos = capacidad;
    }

    // --- SETTERS ---
    public void setId(int id) {
        this.id = id;
    }

    public void setLetra(String letra) {
        if (letra != null) {
            this.letra = letra.trim().toUpperCase();
        } else {
            this.letra = null;
        }
    }

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