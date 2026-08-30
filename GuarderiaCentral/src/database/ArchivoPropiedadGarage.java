package database;

import java.io.File;
import java.io.IOException;

public class ArchivoPropiedadGarage implements Conector{
    private static final String ARCHIVO = "propiedadGarage.txt";
    
    public static String getARHCIVO(){
        return ARCHIVO;
    }
    
    @Override
    public void inicializarBD(){
       try{
          File archivo = new File(ARCHIVO);
           if (!archivo.exists()) {
               if (archivo.createNewFile()) {
                   //System.out.println("Archivo Propiedad Garage creado.");
               }
           }else{
               //System.out.println("Archivo Propiedad Garage existente.");
           }
       }catch(IOException e){
           System.out.println("Error: " + e.getMessage());
       } 
    }
}
