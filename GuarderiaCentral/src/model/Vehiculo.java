package model;

// Importante: asegúrate de importar tu enum
import model.TipoVehiculo;

public class Vehiculo {
    // Atributos
    private int id;
    private int socioId;
    private int empleadoId; // <--- CAMBIO 1: NUEVO ATRIBUTO PARA LA RELACIÓN
    private String nombre;
    private String matricula;
    private TipoVehiculo tipo; // Ahora usamos el Enum
    private float profundidad;
    private float ancho;

    /**
     * Constructor con validaciones defensivas.
     * Actualizado para incluir empleadoId.
     */
    public Vehiculo(int id, int socioId, int empleadoId, String nombre, String matricula, TipoVehiculo tipo, float profundidad, float ancho) {
        // Validaciones de integridad numérica
        if (id < 0) throw new IllegalArgumentException("El ID no puede ser negativo.");
        if (socioId < 0) throw new IllegalArgumentException("El ID del socio no puede ser negativo.");
        if (empleadoId < 0) throw new IllegalArgumentException("El ID del empleado responsable no puede ser negativo."); // <--- CAMBIO 1: Validación nueva
        if (profundidad <= 0) throw new IllegalArgumentException("La profundidad debe ser un valor positivo.");
        if (ancho <= 0) throw new IllegalArgumentException("El ancho debe ser un valor positivo.");
        
        // Validación de cadenas y objetos
        if (matricula == null || matricula.isEmpty()) throw new IllegalArgumentException("La matrícula no puede estar vacía.");
        if (tipo == null) throw new IllegalArgumentException("El tipo de vehículo es obligatorio.");

        this.id = id;
        this.socioId = socioId;
        this.empleadoId = empleadoId; // <--- CAMBIO 1: Asignación nueva
        this.nombre = nombre;
        this.matricula = matricula;
        this.tipo = tipo;
        this.profundidad = profundidad;
        this.ancho = ancho;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getSocioId() { return socioId; }
    public int getEmpleadoId() { return empleadoId; } // <--- CAMBIO 1: Getter nuevo
    public String getNombre() { return nombre; }
    public String getMatricula() { return matricula; }
    public TipoVehiculo getTipo() { return tipo; } // Devuelve el Enum
    public float getProfundidad() { return profundidad; }
    public float getAncho() { return ancho; }
    

    @Override
    public String toString() {
        return "Vehiculo{" + 
                "id=" + id + 
                ", socioId=" + socioId + 
                ", empleadoId=" + empleadoId + // <--- CAMBIO 1: Añadido al toString
                ", nombre='" + nombre + '\'' + 
                ", matricula='" + matricula + '\'' + 
                ", tipo=" + tipo + // Java imprime el nombre del Enum automáticamente
                ", profundidad=" + profundidad + 
                ", ancho=" + ancho + 
                '}';
    }

   /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato Actualizado: id,socioId,empleadoId,nombre,matricula,tipo,profundidad,ancho
     * NOTA: Deberás actualizar tu archivo de texto existente.
     */
    public String toCsv() {
        // Concatenamos los atributos usando comas como separador
        return id + "," + 
               socioId + "," + 
               empleadoId + "," + // <--- CAMBIO 2: Añadido al CSV
               nombre + "," + 
               matricula + "," + 
               tipo + "," + // El Enum se guarda como su nombre (ej. AUTO)
               profundidad + "," + 
               ancho;
    }

    /**
     * Crea un objeto Vehiculo desde una línea de archivo CSV.
     * Formato Actualizado: id,socioId,empleadoId,nombre,matricula,tipo,profundidad,ancho
     */
    public static Vehiculo fromString(String linea) {
        String[] datos = linea.split(",");
        
        // Asumiendo formato CSV: id, socioId, empleadoId, nombre, matricula, tipo, profundidad, ancho
        // Usamos .toUpperCase() para asegurarnos que coincida con el Enum aunque el texto en el archivo esté en minúsculas
        return new Vehiculo(
                Integer.parseInt(datos[0]), // id
                Integer.parseInt(datos[1]), // socioId
                Integer.parseInt(datos[2]), // <--- CAMBIO 2: Leemos el empleadoId (nuevo índice 2)
                datos[3],                   // nombre (índice desplazado)
                datos[4],                   // matricula
                TipoVehiculo.valueOf(datos[5].toUpperCase()), // tipo
                Float.parseFloat(datos[6]), // profundidad
                Float.parseFloat(datos[7])  // ancho
        );
    }
}