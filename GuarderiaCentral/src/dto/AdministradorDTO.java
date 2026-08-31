package dto;

import model.Rol; // Importante: Asegúrate de importar el Enum Rol

public class AdministradorDTO extends UsuarioDTO {

    public AdministradorDTO() {
        super();
    }

    public AdministradorDTO(int id, String nombre, String direccion, 
                            String telefono, String nombreUsuario, String clave, Rol rol) {
        // Pasamos el Rol al super constructor de UsuarioDTO
        super(id, nombre, direccion, telefono, nombreUsuario, clave, rol);
    }
      
}