package util;

import java.io.*;
import java.nio.file.*;

/**
 * Generador de IDs únicos basado en archivos.
 * La base es dictada por el servicio que lo invoca.
 */
public class IdGenerator {

    private static final String RUTA_ARCHIVOS = "data/";

    /**
     * @param tipo El nombre del archivo (ej: "administrador", "garage").
     * @param base El número donde debería empezar si el archivo no existe.
     */
    public static int obtenerNuevoId(String tipo, int base) {
        String archivo = RUTA_ARCHIVOS + "id_" + tipo + ".txt";
        
        int ultimoId = leerUltimoId(archivo, base);
        int nuevoId = ultimoId + 1;
        
        guardarUltimoId(archivo, nuevoId);
        return nuevoId;
    }

    private static int leerUltimoId(String ruta, int base) {
        File file = new File(ruta);
        // Si no existe el archivo, el "último" se considera base - 1 
        // para que la suma resulte exactamente en el valor de la base.
        if (!file.exists()) {
            return base - 1; 
        }
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            return Integer.parseInt(br.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            // Si el archivo está corrupto o vacío, reinicia desde base - 1
            return base - 1;
        }
    }

    private static void guardarUltimoId(String ruta, int id) {
        try {
            Files.createDirectories(Paths.get(RUTA_ARCHIVOS));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
                bw.write(String.valueOf(id));
            }
        } catch (IOException e) {
            System.err.println("Error al persistir el ID: " + e.getMessage());
        }
    }
}