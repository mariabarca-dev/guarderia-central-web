package model;

public class AsignacionEmpleadoZona {
    
    private Empleado empleado;
    private Zona zona;
    private int cantVehiculosACargo;

    /**
     * Constructor con validación.
     * @throws IllegalArgumentException si los datos son inválidos.
     */
    public AsignacionEmpleadoZona(Empleado empleado, Zona zona, int cantVehiculosACargo) {
        // Validación de nulidad (importante para evitar errores en otras partes)
        if (empleado == null || zona == null) {
            throw new IllegalArgumentException("El empleado y la zona no pueden ser nulos.");
        }
        // Validación de números negativos
        if (cantVehiculosACargo < 0) {
            throw new IllegalArgumentException("La cantidad de vehículos a cargo no puede ser negativa.");
        }
        
        this.empleado = empleado;
        this.zona = zona;
        this.cantVehiculosACargo = cantVehiculosACargo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Zona getZona() {
        return zona;
    }

    public int getCantVehiculosACargo() {
        return cantVehiculosACargo;
    }

 
    
    
    
    @Override
    public String toString() {
        return "Asignacion de Empleado por Zona{" +
                "codigo de empleado='" + empleado.getCodigo() + '\'' +
                ", Letra de Zona='" + zona.getLetra() + '\'' +
                ", Cantidad de Vehiculos a Cargo=" + cantVehiculosACargo +
                '}';
    }
    
    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: idEmpleado,idZona,cantidadVehiculos
     */
    public String toCsv() {
        // IMPORTANTE: Para la persistencia en archivo, guardamos SOLO LOS IDs.
        // Asumimos que Empleado y Zona tienen un método getId() que retorna un entero.
        return empleado.getId() + "," + 
               zona.getId() + "," + 
               cantVehiculosACargo;
    }
    
    
    // --- Método factory CORREGIDO ---
    /**
     * Crea un objeto Asignacion partiendo de una línea CSV y los objetos ya recuperados de sus DAOs.
     */
    public static AsignacionEmpleadoZona fromString(String linea, Empleado empleado, Zona zona) {
        String[] datos = linea.split(",");
        // datos[0] es idEmpleado, datos[1] es idZona (ambos ya usados para buscar los objetos pasados por parámetro)
        int cantidad = Integer.parseInt(datos[2]); // datos[2] es la cantidad
        
        // El constructor validará automáticamente la cantidad
        return new AsignacionEmpleadoZona(empleado, zona, cantidad);
    }
    
}
