package dto;

import model.Rol; // Asegúrate de importar el Enum Rol

public class EmpleadoDTO extends UsuarioDTO {

    private String codigo;
    private String especialidad;

    // Constructor actualizado con el parámetro Rol
    public EmpleadoDTO(int id, String nombre, String direccion, String telefono, 
                       String nombreUsuario, String clave, Rol rol, 
                       String codigo, String especialidad) {
        
        // Pasamos el rol al constructor de la clase padre (UsuarioDTO)
        super(id, nombre, direccion, telefono, nombreUsuario, clave, rol);
        
        this.codigo = codigo;
        this.especialidad = especialidad;
    }
    
    // Getters y Setters
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
}