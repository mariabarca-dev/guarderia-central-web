package model;

public class Administrador extends Usuario {

    // Constructor vacío
    public Administrador() {
        super();
    }

    // Constructor sin id (incluye apellido)
    public Administrador(String nombre, String apellido, String direccion, String telefono,
                         String nombreUsuario, String clave, Rol rol) {
        super(nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
    }

    // Constructor con parámetros completo (incluye id y apellido)
    public Administrador(int id, String nombre, String apellido, String direccion, String telefono,
                         String nombreUsuario, String clave, Rol rol) {
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
    }

    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,nombre,apellido,direccion,telefono,nombreUsuario,clave,ROL
     */
    public String toCsv() {
        return getId() + "," +
                getNombre() + "," +
                getApellido() + "," +
                getDireccion() + "," +
                getTelefono() + "," +
                getNombreUsuario() + "," +
                getClave() + "," +
                getRol();
    }

    // Método factory para leer desde archivo
    public static Administrador fromString(String linea) {
        String[] datos = linea.split(",");

        return new Administrador(
                Integer.parseInt(datos[0].trim()), // id
                datos[1].trim(),                   // nombre
                datos[2].trim(),                   // apellido
                datos[3].trim(),                   // direccion
                datos[4].trim(),                   // telefono
                datos[5].trim(),                   // nombreUsuario
                datos[6].trim(),                   // clave
                Rol.valueOf(datos[7].trim())       // Rol
        );
    }
}