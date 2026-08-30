
package database;

import java.io.File;
import java.io.IOException;

public class ArchivoVehiculo implements Conector {
    private static final String ARCHIVO = "vehiculo.txt";

    public static String getARCHIVO() {
        return ARCHIVO;
    }

    @Override
    public void inicializarBD() {
        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    //System.out.println("Archivo Vehiculo creado.");
                }
            } else {
                //System.out.println("Archivo Vehiculo existente.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
 

