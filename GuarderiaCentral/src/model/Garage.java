package model;

import java.time.LocalDate;
import java.util.Objects;

public class Garage {

    private int id;
    private int numeroGarage;
    private double lecturaLuz;
    private boolean servicioMantenimiento;
    private Socio socioPropietario; // Puede ser null si está libre
    private LocalDate fechaCompra;
    private Zona zona; 

    /**
     * Constructor completo con validaciones defensivas.
     */
    public Garage(int id, int numeroGarage, double lecturaLuz, boolean servicioMantenimiento, 
                  Socio socioPropietario, LocalDate fechaCompra, Zona zona) {
        // Validaciones numéricas
        if (id < 0) throw new IllegalArgumentException("El ID no puede ser negativo.");
        if (numeroGarage <= 0) throw new IllegalArgumentException("El número de garaje debe ser mayor a 0.");
        if (lecturaLuz < 0) throw new IllegalArgumentException("La lectura de luz no puede ser negativa.");
        
        // Validación de objetos obligatorios
        Objects.requireNonNull(fechaCompra, "La fecha de compra no puede ser nula.");
        Objects.requireNonNull(zona, "Un garaje debe pertenecer a una zona.");
        
        this.id = id;
        this.numeroGarage = numeroGarage;
        this.lecturaLuz = lecturaLuz;
        this.servicioMantenimiento = servicioMantenimiento;
        this.socioPropietario = socioPropietario;
        this.fechaCompra = fechaCompra;
        this.zona = zona; // ¡ASIGNACIÓN DEL NUEVO ATRIBUTO!
    }

    /**
     * Constructor sin lectura de luz (por si se crea antes de la primera medición)
     */
    public Garage(int id, int numeroGarage, boolean servicioMantenimiento, 
                  Socio socioPropietario, LocalDate fechaCompra, Zona zona) {
        this(id, numeroGarage, 0.0, servicioMantenimiento, socioPropietario, fechaCompra, zona);
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getNumeroGarage() { return numeroGarage; }
    public double getLecturaLuz() { return lecturaLuz; }
    public boolean isServicioMantenimiento() { return servicioMantenimiento; }
    public Socio getSocioPropietario() { return socioPropietario; }
    public LocalDate getFechaCompra() { return fechaCompra; }
    public Zona getZona() { return zona; } 

 

    @Override
    public String toString() {
        int idSocio = (socioPropietario != null) ? socioPropietario.getId() : 0;
        // Incluimos la letra de la zona en el toString
        String letraZona = (zona != null) ? zona.getLetra() : "SIN ZONA";
        
        return "Garage{" +
                "id=" + id +
                ", numeroGarage=" + numeroGarage +
                ", zona=" + letraZona + // Ahora mostramos la zona
                ", lecturaLuz=" + lecturaLuz +
                ", servicioMantenimiento=" + servicioMantenimiento +
                ", idSocioPropietario=" + idSocio +
                ", fechaCompra=" + fechaCompra +
                '}';
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,numeroGarage,lecturaLuz,servicioMantenimiento,idSocio,fechaCompra,idZona
     */
    public String toCsv() {
        int idSocio = (socioPropietario != null) ? socioPropietario.getId() : 0;
        int idZona = (zona != null) ? zona.getId() : 0;

        return id + "," + 
               numeroGarage + "," + 
               lecturaLuz + "," + 
               servicioMantenimiento + "," + 
               idSocio + "," + 
               fechaCompra + "," + 
               idZona;
    }

    /**
     * Crea un objeto Garage desde una línea de archivo CSV.
     */
    public static Garage fromString(String linea, Socio socio, Zona zona) {
        String[] datos = linea.split(",");
        
        // Asumiendo formato CSV: id,numeroGarage,lecturaLuz,servicioMantenimiento,idSocio,fechaCompra,idZona
        // Nota: idSocio y idZona ya fueron usados por el DAO para buscar los objetos 'socio' y 'zona' pasados aquí.
        return new Garage(
                Integer.parseInt(datos[0]), // id
                Integer.parseInt(datos[1]), // numeroGarage
                Double.parseDouble(datos[2]), // lecturaLuz
                Boolean.parseBoolean(datos[3]), // servicioMantenimiento
                socio, // Objeto Socio inyectado
                LocalDate.parse(datos[5]), // fechaCompra
                zona // Objeto Zona inyectado
        );
    }
}