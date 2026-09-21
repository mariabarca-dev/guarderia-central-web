package database;


import java.io.File;
import java.io.IOException;


public class ArchivoSocio implements Conector{
    
    private static final String ARCHIVO = "socios.txt";
    
    public static String getARCHIVO(){
        return ARCHIVO;
    }
    
    @Override
    public void inicializarBD(){
        try{
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) {
                if (archivo.createNewFile()) {
                    //System.out.println("Archivo Socio creado.");
                }
            }else{
                //System.out.println("Archivo Socio existente.");
            }
        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
