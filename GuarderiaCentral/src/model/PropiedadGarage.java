
package model;

import java.time.LocalDate;

public class PropiedadGarage {
    

    private Socio socio;
    private Garage garage;
    private LocalDate fechaCompraGarage;

    public PropiedadGarage(Socio socio, Garage garage, LocalDate fechaCompraGarage) {
        this.socio = socio;
        this.garage = garage;
        this.fechaCompraGarage= fechaCompraGarage;
    }

    public Socio getSocio() {
        return socio;
    }

    public Garage getGarage() {
        return garage;
    }

    public LocalDate getFechaCompraGarage() {
        return fechaCompraGarage;
    }

    @Override
    public String toString() {
        return "Propiedad Garage{"+
                "Dni Socio='" + socio.getDni()+ '\'' +
                "Nro de Garage='" + garage.getNumeroGarage()+ '\'' +
                "fecha compra garage='" + fechaCompraGarage + '\'' +
                '}';
                       
    }

    
    
    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: idSocio,idGarage,fechaCompra
     */
    public String toCsv() {
        // Obtenemos los IDs para guardarlos en el archivo (asumiendo que los DAOs buscarán por ID luego)
        int idSocio = (socio != null) ? socio.getId() : 0;
        int idGarage = (garage != null) ? garage.getId() : 0;

        return idSocio + "," + 
               idGarage + "," + 
               fechaCompraGarage;
    }

    // --- Método factory CORREGIDO ---
    /**
     * Crea un objeto PropiedadGarage partiendo de una línea CSV y los objetos ya recuperados de sus DAOs.
     */
    public static PropiedadGarage fromString(String linea, Socio socio, Garage garage) {
        String[] datos  =  linea.split(",");
        // datos[0] es idSocio, datos[1] es idGarage (ambos ya usados para buscar los objetos pasados por parámetro)
        
        return new PropiedadGarage (
                socio,
                garage,
                LocalDate.parse(datos[2]) // datos[2] es la fecha
        );
    }
               

 }



