package model;

public class Administrador extends Usuario {

   

    // Constructor con parámetros (AHORA INCLUYE ROL)
    public Administrador(int id, String nombre, String direccion, String telefono, 
                         String nombreUsuario, String clave, Rol rol) {
        // Pasamos el Rol al super constructor de Usuario
        super(id, nombre, direccion, telefono, nombreUsuario, clave, rol);
    }

    
    
    
    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }
    
    
    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,nombre,direccion,telefono,nombreUsuario,clave,ROL
     */
    public String toCsv() {
        // Obtenemos los datos del padre (super) y los propios
        return getId() + "," + 
               getNombre() + "," + 
               getDireccion() + "," + 
               getTelefono() + "," + 
               getNombreUsuario() + "," + 
               getClave() + "," + 
               getRol(); // El Enum Rol se imprime como su nombre (ADMINISTRADOR)
    }
    

    // Método factory para leer desde archivo
    public static Administrador fromString(String linea) {
        String[] datos = linea.split(",");

        return new Administrador(
            Integer.parseInt(datos[0]), // id
            datos[1],                   // nombre
            datos[2],                   // direccion
            datos[3],                   // telefono
            datos[4],                   // nombreUsuario
            datos[5],                   // clave
            Rol.valueOf(datos[6])        //  el Rol 
        );
    }
}