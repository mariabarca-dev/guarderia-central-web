package model;

public class Empleado extends Usuario {

    private String codigo;
    private String especialidad;

    
    // Constructor con parámetros (AHORA INCLUYE ROL)
    public Empleado(int id, String nombre, String direccion, String telefono, 
                    String nombreUsuario, String clave, Rol rol, String codigo, String especialidad) {
        // Pasamos el Rol al super constructor de Usuario
        super(id, nombre, direccion, telefono, nombreUsuario, clave, rol); 
        this.codigo = codigo;
        this.especialidad = especialidad;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    @Override
    public String toString() {
        return "Empleado{" + super.toString() + 
               ", codigo='" + codigo + '\'' + 
               ", especialidad='" + especialidad + '\'' + '}';
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,nombre,direccion,telefono,nombreUsuario,clave,ROL,codigo,especialidad
     */
    public String toCsv() {
        // Obtenemos los datos del padre (super) y los propios, separados por coma
        return getId() + "," + 
               getNombre() + "," + 
               getDireccion() + "," + 
               getTelefono() + "," + 
               getNombreUsuario() + "," + 
               getClave() + "," + 
               getRol() + "," + // El Enum Rol se guarda como su nombre (EMPLEADO)
               codigo + "," + 
               especialidad;
    }

   // Método factory para leer desde archivo
    public static Empleado fromString(String linea) {
        // Si una línea está vacía, la ignoramos para evitar errores
        if (linea == null || linea.trim().isEmpty()) return null;
        String[] datos = linea.split(",");
        // CORREGIDO: Asegurar que tenemos los 9 campos esperados y los índices correctos
        if (datos.length != 9) {
            throw new IllegalArgumentException("Línea de empleado inválida: " + linea);
        }
        // Se asume formato CSV: id,nombre,direccion,telefono,nombreUsuario,clave,rol,codigo,especialidad
        return new Empleado(
            Integer.parseInt(datos[0]), // id (pos 0)
            datos[1],                   // nombre (pos 1)
            datos[2],                   // direccion (pos 2)
            datos[3],                   // telefono (pos 3)
            datos[4],                   // nombreUsuario (pos 4)
            datos[5],                   // clave (pos 5)
            Rol.valueOf(datos[6]),      // El Rol se lee del archivo usando valueOf (pos 6)
            datos[7],                   // codigo (ahora en pos 7)
            datos[8]                    // especialidad (ahora en pos 8)
        );
    }
}