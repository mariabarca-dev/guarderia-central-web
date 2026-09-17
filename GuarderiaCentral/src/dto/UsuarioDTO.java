package dto;

import model.Rol;

public class UsuarioDTO {

    private int id;
    private String nombre;
    private String apellido; // <--- Nuevo atributo
    private String direccion;
    private String telefono;
    private String nombreUsuario;
    private String clave;
    private Rol rol;

    public UsuarioDTO() {
    }

    public UsuarioDTO(int id, String nombre, String apellido, String direccion, String telefono,
                      String nombreUsuario, String clave, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido; // <--- Inicialización
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; } // <--- Nuevo Getter
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getClave() { return clave; }
    public Rol getRol() { return rol; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; } // <--- Nuevo Setter
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setClave(String clave) { this.clave = clave; }
    public void setRol(Rol rol) { this.rol = rol; }
}