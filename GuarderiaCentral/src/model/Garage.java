package model;

import java.time.LocalDate;
import java.util.Objects;

public class Garage {

    /** Valor que se escribe en el TXT cuando el garaje todavía no fue vendido. */
    private static final String SIN_FECHA = "SIN_FECHA";

    private int id;
    private int numeroGarage;
    private double lecturaLuz;
    private boolean servicioMantenimiento;
    private Socio socioPropietario; // Puede ser null si está libre
    private LocalDate fechaCompra;  // Puede ser null si todavía no se vendió
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
        Objects.requireNonNull(zona, "Un garaje debe pertenecer a una zona.");

        this.id = id;
        this.numeroGarage = numeroGarage;
        this.lecturaLuz = lecturaLuz;
        this.servicioMantenimiento = servicioMantenimiento;
        this.socioPropietario = socioPropietario;
        this.fechaCompra = fechaCompra;
        this.zona = zona;
    }

    /**
     * Constructor sin lectura de luz (por si se crea antes de la primera medición).
     */
    public Garage(int id, int numeroGarage, boolean servicioMantenimiento,
                  Socio socioPropietario, LocalDate fechaCompra, Zona zona) {
        this(id, numeroGarage, 0.0, servicioMantenimiento, socioPropietario, fechaCompra, zona);
    }

    //constructor Vacio
    public Garage() {
    }

    //constructor sin ID
    public Garage(int numeroGarage, double lecturaLuz, boolean servicioMantenimiento,
                  Socio socioPropietario, LocalDate fechaCompra, Zona zona) {
        if (numeroGarage <= 0) throw new IllegalArgumentException("El número de garaje debe ser mayor a 0.");
        if (lecturaLuz < 0) throw new IllegalArgumentException("La lectura de luz no puede ser negativa.");

        Objects.requireNonNull(zona, "Un garaje debe pertenecer a una zona.");

        this.numeroGarage = numeroGarage;
        this.lecturaLuz = lecturaLuz;
        this.servicioMantenimiento = servicioMantenimiento;
        this.socioPropietario = socioPropietario;
        this.fechaCompra = fechaCompra;
        this.zona = zona;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getNumeroGarage() { return numeroGarage; }
    public double getLecturaLuz() { return lecturaLuz; }
    public boolean isServicioMantenimiento() { return servicioMantenimiento; }
    public Socio getSocioPropietario() { return socioPropietario; }
    public LocalDate getFechaCompra() { return fechaCompra; }
    public Zona getZona() { return zona; }

    /** Un garaje está vendido cuando tiene socio propietario asignado. */
    public boolean estaVendido() {
        return socioPropietario != null;
    }

    @Override
    public String toString() {
        int idSocio = (socioPropietario != null) ? socioPropietario.getId() : 0;
        String letraZona = (zona != null) ? zona.getLetra() : "SIN ZONA";
        String fecha = (fechaCompra != null) ? fechaCompra.toString() : "sin vender";

        return "Garage{" +
                "id=" + id +
                ", numeroGarage=" + numeroGarage +
                ", zona=" + letraZona +
                ", lecturaLuz=" + lecturaLuz +
                ", servicioMantenimiento=" + servicioMantenimiento +
                ", idSocioPropietario=" + idSocio +
                ", fechaCompra=" + fecha +
                '}';
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Formato: id,numeroGarage,lecturaLuz,servicioMantenimiento,idSocio,fechaCompra,idZona
     * Si el garaje no fue vendido, la fecha se persiste como SIN_FECHA.
     */
    public String toCsv() {
        int idSocio = (socioPropietario != null) ? socioPropietario.getId() : 0;
        int idZona = (zona != null) ? zona.getId() : 0;
        String fecha = (fechaCompra != null) ? fechaCompra.toString() : SIN_FECHA;

        return id + "," +
                numeroGarage + "," +
                lecturaLuz + "," +
                servicioMantenimiento + "," +
                idSocio + "," +
                fecha + "," +
                idZona;
    }

    /**
     * Crea un objeto Garage desde una línea de archivo CSV.
     * El DAO ya resolvió los objetos Socio y Zona a partir de los IDs de la línea.
     */
    public static Garage fromString(String linea, Socio socio, Zona zona) {
        String[] datos = linea.split(",");

        if (datos.length < 7) {
            throw new IllegalArgumentException("La línea no tiene los 7 campos esperados: " + linea);
        }

        return new Garage(
                Integer.parseInt(datos[0].trim()),      // id
                Integer.parseInt(datos[1].trim()),      // numeroGarage
                Double.parseDouble(datos[2].trim()),    // lecturaLuz
                Boolean.parseBoolean(datos[3].trim()),  // servicioMantenimiento
                socio,
                parsearFecha(datos[5]),                 // fechaCompra (puede ser null)
                zona
        );
    }

    /**
     * Convierte el campo fecha del TXT a LocalDate.
     * Devuelve null si está vacío, si dice SIN_FECHA o si quedó la palabra "null"
     * escrita por versiones anteriores del archivo.
     */
    private static LocalDate parsearFecha(String valor) {
        if (valor == null) return null;

        String v = valor.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("null") || v.equalsIgnoreCase(SIN_FECHA)) {
            return null;
        }
        return LocalDate.parse(v);
    }
}