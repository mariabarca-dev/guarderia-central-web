package model;

import java.time.LocalDate;

public class Socio extends Usuario {

    private String dni;
    private LocalDate fechaIngreso;

   

    // Constructor con parámetros (AHORA INCLUYE EL ROL)
    public Socio(int id, String nombre, String direccion, String telefono,
                 String nombreUsuario, String clave, Rol rol, String dni, LocalDate fechaIngreso) {
        // Pasamos el Rol al super constructor de Usuario
        super(id, nombre, direccion, telefono, nombreUsuario, clave, rol); 
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters
    public String getDni() {
        return dni;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    @Override
    public String toString() {
        // Usamos el toString del padre y agregamos los campos propios
        return "Socio{" + super.toString() + 
               ", dni='" + dni + '\'' + 
               ", fechaIngreso=" + fechaIngreso + '}';
    }

    /**
     * --- ESTE ES PARA EL ARCHIVO TXT ---
     * Genera una línea simple separada por comas para la persistencia.
     * Formato: id,nombre,direccion,telefono,nombreUsuario,clave,ROL,dni,fechaIngreso
     */
    public String toCsv() {
        // Obtenemos los datos del padre (super) y los propios, separados por coma
        return getId() + "," + 
               getNombre() + "," + 
               getDireccion() + "," + 
               getTelefono() + "," + 
               getNombreUsuario() + "," + 
               getClave() + "," + 
               getRol() + "," + // El Enum Rol se guarda como su nombre (SOCIO)
               dni + "," + 
               fechaIngreso; // LocalDate se convierte a String automáticamente (formato ISO)
    }

   // Método factory para leer desde archivo
    public static Socio fromString(String linea) {
        if (linea == null || linea.trim().isEmpty()) return null;
        String[] datos = linea.split(",");
        // CORREGIDO: Asegurar que tenemos los 9 campos esperados
        if (datos.length != 9) {
            throw new IllegalArgumentException("Línea de socio inválida: " + linea);
        }
        // Se asume formato CSV: id,nombre,direccion,telefono,nombreUsuario,clave,rol,dni,fechaIngreso
        return new Socio(
            Integer.parseInt(datos[0]), // id (pos 0)
            datos[1],                   // nombre (pos 1)
            datos[2],                   // direccion (pos 2)
            datos[3],                   // telefono (pos 3)
            datos[4],                   // nombreUsuario (pos 4)
            datos[5],                   // clave (pos 5)
            Rol.valueOf(datos[6]),      // CORREGIDO: Usar valueOf para leer el rol real (pos 6)
            datos[7],                   // dni (pos 7)
            LocalDate.parse(datos[8])   // fechaIngreso (pos 8)
        );
    }
}