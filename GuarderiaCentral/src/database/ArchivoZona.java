package database;

import java.io.File;
import java.io.IOException;

public class ArchivoZona implements Conector {

    private static final String ARCHIVO = "zona.txt";

    public static String getARCHIVO() {
        return ARCHIVO;
    }

    @Override
    public void inicializarBD() {
        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    //System.out.println("Archivo Zona creado.");
                }
            } else {
                //System.out.println("Archivo Zona existente.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
