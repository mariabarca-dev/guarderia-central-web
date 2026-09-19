package dto;

import model.Rol; // Importante: Asegúrate de importar el Enum Rol

public class AdministradorDTO extends UsuarioDTO {

    // Constructor vacío
    public AdministradorDTO() {
        super();
    }

    // Constructor completo (incluye apellido heredado de UsuarioDTO)
    public AdministradorDTO(int id, String nombre, String apellido, String direccion,
                            String telefono, String nombreUsuario, String clave, Rol rol) {
        // Pasamos todos los atributos al constructor de la clase padre (UsuarioDTO)
        super(id, nombre, apellido, direccion, telefono, nombreUsuario, clave, rol);
    }
}