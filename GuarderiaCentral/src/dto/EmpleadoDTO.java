package dto;

import model.Rol; // Importante: Asegúrate de importar el Enum Rol

public class EmpleadoDTO extends UsuarioDTO {

    private String codigo;
    private String especialidad;

    // Constructor vacío
    public EmpleadoDTO() {
        super();
    }

    // Constructor parametrizado (incluye apellido heredado de UsuarioDTO)
    public EmpleadoDTO(int id, String nombre, String apellido, String direccion, String telefono,
                       String nombreUsuario, String clave, Rol rol,
                       String codigo, String especialidad) {

        // Pasamos los atributos base (incluyendo apellido) a UsuarioDTO
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);

        this.codigo = codigo;
        this.especialidad = especialidad;
    }

    // Getters y Setters propios de EmpleadoDTO
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