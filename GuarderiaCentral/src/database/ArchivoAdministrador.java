package database;

import java.io.File;
import java.io.IOException;

public class ArchivoAdministrador implements Conector {

    private static final String ARCHIVO = "administrador.txt";

    public static String gestARCHIVO() {
        return ARCHIVO;
    }
    
    @Override
    public void inicializarBD() {

        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    //System.out.println("Archivo Administrador creado.");
                }
            }else{
                //System.out.println("Archivo Administrador existente.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

