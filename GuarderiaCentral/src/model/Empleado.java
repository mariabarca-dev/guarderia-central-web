package model;

public class Empleado extends Usuario {

    private String codigo;
    private String especialidad;

    // Constructor vacío
    public Empleado() {
        super();
    }

    // Constructor sin id (incluye apellido)
    public Empleado(String nombre, String apellido, String direccion, String telefono,
                    String nombreUsuario, String clave, Rol rol, String codigo, String especialidad) {
        super(nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
        this.codigo = codigo;
        this.especialidad = especialidad;
    }

    // Constructor con parámetros completo (incluye id y apellido)
    public Empleado(int id, String nombre, String apellido, String direccion, String telefono,
                    String nombreUsuario, String clave, Rol rol, String codigo, String especialidad) {
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
        this.codigo = codigo;
        this.especialidad = especialidad;
    }

    // Getters y Setters propios de Empleado
    public String getCodigo() {
        return codigo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
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
     * Formato: id,nombre,apellido,direccion,telefono,nombreUsuario,clave,ROL,codigo,especialidad
     */
    public String toCsv() {
        return getId() + "," +
                getNombre() + "," +
                getApellido() + "," +
                getDireccion() + "," +
                getTelefono() + "," +
                getNombreUsuario() + "," +
                getClave() + "," +
                getRol() + "," +
                codigo + "," +
                especialidad;
    }

    // Método factory para leer desde archivo
    public static Empleado fromString(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        String[] datos = linea.split(",");

        // Ahora se esperan 10 campos por la inclusión del apellido
        if (datos.length != 10) {
            throw new IllegalArgumentException("Línea de empleado inválida: " + linea);
        }

        // Formato CSV: id,nombre,apellido,direccion,telefono,nombreUsuario,clave,rol,codigo,especialidad
        return new Empleado(
                Integer.parseInt(datos[0].trim()), // id (pos 0)
                datos[1].trim(),                   // nombre (pos 1)
                datos[2].trim(),                   // apellido (pos 2)
                datos[3].trim(),                   // direccion (pos 3)
                datos[4].trim(),                   // telefono (pos 4)
                datos[5].trim(),                   // nombreUsuario (pos 5)
                datos[6].trim(),                   // clave (pos 6)
                Rol.valueOf(datos[7].trim()),      // Rol (pos 7)
                datos[8].trim(),                   // codigo (pos 8)
                datos[9].trim()                    // especialidad (pos 9)
        );
    }
}