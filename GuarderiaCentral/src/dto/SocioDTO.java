package dto;

import model.Rol; // Importante: Asegúrate de importar el Enum Rol
import java.time.LocalDate;

public class SocioDTO extends UsuarioDTO {

    private String dni;
    private LocalDate fechaIngreso;

    // Constructor actualizado con el parámetro Rol
    public SocioDTO(int id, String nombre, String direccion, String telefono, 
                    String nombreUsuario, String clave, Rol rol, 
                    String dni, LocalDate fechaIngreso) {
        
        // Pasamos el rol al constructor de la clase padre (UsuarioDTO)
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

    // Setters
    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}