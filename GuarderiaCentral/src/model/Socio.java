package model;

import java.time.LocalDate;

public class Socio extends Usuario {

    private String dni;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public Socio() {
        super();
    }

    // Constructor sin id (incluye apellido)
    public Socio(String nombre, String apellido, String direccion, String telefono,
                 String nombreUsuario, String clave, Rol rol, String dni, LocalDate fechaIngreso) {
        super(nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // Constructor con parámetros completo (incluye id y apellido)
    public Socio(int id, String nombre, String apellido, String direccion, String telefono,
                 String nombreUsuario, String clave, Rol rol, String dni, LocalDate fechaIngreso) {
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters propios de Socio
    public String getDni() {
        return dni;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return "Socio{" + super.toString() +
                ", dni='" + dni + '\'' +
                ", fechaIngreso=" + fechaIngreso + '}';
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,nombre,apellido,direccion,telefono,nombreUsuario,clave,ROL,dni,fechaIngreso
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
                dni + "," +
                fechaIngreso;
    }

    // Método factory para leer desde archivo
    public static Socio fromString(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;

        String[] datos = linea.split(",");

        // Ahora se esperan 10 campos por la inclusión del apellido
        if (datos.length != 10) {
            throw new IllegalArgumentException("Línea de socio inválida: " + linea);
        }

        // Formato CSV: id,nombre,apellido,direccion,telefono,nombreUsuario,clave,rol,dni,fechaIngreso
        return new Socio(
                Integer.parseInt(datos[0].trim()), // id (pos 0)
                datos[1].trim(),                   // nombre (pos 1)
                datos[2].trim(),                   // apellido (pos 2)
                datos[3].trim(),                   // direccion (pos 3)
                datos[4].trim(),                   // telefono (pos 4)
                datos[5].trim(),                   // nombreUsuario (pos 5)
                datos[6].trim(),                   // clave (pos 6)
                Rol.valueOf(datos[7].trim()),      // Rol (pos 7)
                datos[8].trim(),                   // dni (pos 8)
                LocalDate.parse(datos[9].trim())   // fechaIngreso (pos 9)
        );
    }
}