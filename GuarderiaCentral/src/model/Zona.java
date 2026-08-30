package model;

import model.TipoVehiculo; // Importamos el Enum

public class Zona {

    private int id;
    private String letra;
    private TipoVehiculo tipoVehiculo; // Ahora usamos el Enum
    private int capacidadVehiculos;
    private float anchoGarage;
    private float largoGarage;

    
    public Zona() {
    }

    
    /**
     * Constructor con validación de datos.
     */
    public Zona(int id, String letra, TipoVehiculo tipoVehiculo, int capacidadVehiculos, float anchoGarage, float largoGarage) {
        // Validaciones defensivas
        if (id < 0) throw new IllegalArgumentException("El ID no puede ser negativo.");
        if (capacidadVehiculos < 0) throw new IllegalArgumentException("La capacidad no puede ser negativa.");
        if (anchoGarage <= 0) throw new IllegalArgumentException("El ancho del garage debe ser mayor a 0.");
        if (largoGarage <= 0) throw new IllegalArgumentException("El largo del garage debe ser mayor a 0.");
        if (tipoVehiculo == null) throw new IllegalArgumentException("El tipo de vehículo para la zona es obligatorio.");

        this.id = id;
        this.letra = letra;
        this.tipoVehiculo = tipoVehiculo;
        this.capacidadVehiculos = capacidadVehiculos;
        this.anchoGarage = anchoGarage;
        this.largoGarage = largoGarage;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getLetra() { return letra; }
    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; } // Ahora retorna Enum
    public int getCapacidadVehiculos() { return capacidadVehiculos; }
    public float getAnchoGarage() { return anchoGarage; }
    public float getLargoGarage() { return largoGarage; }

   
    @Override
    public String toString() {
        return "Zona{" +
                "id=" + id +
                ", letra='" + letra + '\'' +
                ", tipoVehiculo=" + tipoVehiculo + // Java imprimirá el nombre del Enum
                ", capacidadVehiculos=" + capacidadVehiculos +
                ", anchoGarage=" + anchoGarage +
                ", largoGarage=" + largoGarage +
                '}';
    }

   /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,letra,tipoVehiculo,capacidadVehiculos,anchoGarage,largoGarage
     */
    public String toCsv() {
        // Concatenamos los atributos usando comas como separador.
        // El Enum se guarda automáticamente como su nombre (ej. "AUTO").
        return id + "," + 
               letra + "," + 
               tipoVehiculo + "," + 
               capacidadVehiculos + "," + 
               anchoGarage + "," + 
               largoGarage;
    }

    /**
     * Crea un objeto Zona desde una línea de archivo CSV.
     */
    public static Zona fromString(String linea) {
        String[] datos = linea.split(",");
        
        // Convertimos el String del archivo a nuestro Enum
        // Usamos .toUpperCase() por seguridad, por si en el archivo escribieron "auto" en minúsculas
        TipoVehiculo tipo = TipoVehiculo.valueOf(datos[2].toUpperCase());

        return new Zona(
                Integer.parseInt(datos[0]), 
                datos[1],                   
                tipo,                       // Pasamos el Enum ya convertido
                Integer.parseInt(datos[3]), 
                Float.parseFloat(datos[4]), 
                Float.parseFloat(datos[5])  
        );
    }
}