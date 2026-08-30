package dto;

import model.Rol; // Asegúrate de importar el Enum

public class UsuarioDTO {
	
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String nombreUsuario; 
    private String clave;
    private Rol rol; 

    public UsuarioDTO(int id, String nombre, String direccion, String telefono, 
                      String nombreUsuario, String clave, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol; // <--- Inicialización
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getClave() { return clave; }
    public Rol getRol() { return rol; } // <--- Getter

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setClave(String clave) { this.clave = clave; }
    public void setRol(Rol rol) { this.rol = rol; } // <--- Setter
}