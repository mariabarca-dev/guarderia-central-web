package dto;

import model.Rol; // Importante: Asegúrate de importar el Enum Rol
import java.time.LocalDate;

public class SocioDTO extends UsuarioDTO {

    private String dni;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public SocioDTO() {
        super();
    }

    // Constructor parametrizado (incluye apellido heredado de UsuarioDTO)
    public SocioDTO(int id, String nombre, String apellido, String direccion, String telefono,
                    String nombreUsuario, String clave, Rol rol,
                    String dni, LocalDate fechaIngreso) {

        // Pasamos los atributos base a la clase padre (UsuarioDTO)
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);

        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters propios de SocioDTO
    public String getDni() {
        return dni;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    // Setters propios de SocioDTO
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}