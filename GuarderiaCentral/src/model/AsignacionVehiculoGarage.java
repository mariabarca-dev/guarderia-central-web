
package model;


import java.time.LocalDate;

public class AsignacionVehiculoGarage {

    private Vehiculo vehiculo;
    private Garage garage;
    private LocalDate fechaAsignacionGarage;

    public AsignacionVehiculoGarage(Vehiculo vehiculo, Garage garage, LocalDate fechaAsignacionGarage) {
        this.vehiculo = vehiculo;
        this.garage = garage;
        this.fechaAsignacionGarage = fechaAsignacionGarage;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Garage getGarage() {
        return garage;
    }

    public LocalDate getFechaAsignacionGarage() {
        return fechaAsignacionGarage;
    }

    @Override
    public String toString() {
        return "Asignacion Vehiculo Garage{"+
                "matricula='" + vehiculo.getMatricula()+ '\'' +
                "numero de garage='" + garage.getNumeroGarage() + '\'' +    
                "fecha asignacion garage='" + fechaAsignacionGarage + '\'' +
                '}';
                        
    }                   
                        
     /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: idVehiculo,idGarage,fechaAsignacion
     * Nota: Se asume que Vehiculo y Garage tienen un método getId().
     */
    public String toCsv() {
        return vehiculo.getId() + "," + 
               garage.getId() + "," + 
               fechaAsignacionGarage;
    }
    
     // --- Método factory CORREGIDO ---
    /**
     * Crea un objeto Asignacion partiendo de una línea CSV y los objetos ya recuperados de sus DAOs.
     */
    public static AsignacionVehiculoGarage fromString(String linea, Vehiculo vehiculo, Garage garage) {
        String[] datos = linea.split(",");
        // datos[0] es idVehiculo, datos[1] es idGarage (ambos ya usados para buscar los objetos pasados por parámetro)
        
        return new AsignacionVehiculoGarage( 
            vehiculo,
            garage,
            LocalDate.parse(datos[2]) // datos[2] es la fecha
        );
    }
}

