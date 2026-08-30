package database;

import java.io.File;
import java.io.IOException;

public class ArchivoAsignacionEmpleadoZona implements Conector{
    private static final String ARCHIVO = "asignacionEmpleadoZona.txt";

    public static String getARCHIVO(){
      return ARCHIVO;  
    }
    
    @Override
    public void inicializarBD(){
        try{
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    //System.out.println("Archivo Asignacion Empleado Zona creado.");
                }
            }else{
                //System.out.println("Archivo Asignacion Empleado Zona existente.");
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
