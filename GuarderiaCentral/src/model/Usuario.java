package model;

import java.io.Serializable;

public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
	
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String nombreUsuario; // Usaremos esto como el "usuario" 
    private String clave;         // La contraseña
    private Rol rol;              // Nuevo atributo

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con parámetros sin id
    public Usuario(String nombre, String direccion, String telefono, String nombreUsuario, String clave, Rol rol) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol;
    }
    // Constructor con parámetros
    public Usuario(int id, String nombre, String direccion, String telefono, String nombreUsuario, String clave, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombreUsuario() { 
        return nombreUsuario; 
    }
    
    public String getClave() {
        return clave; 
    }
    
    public Rol getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", rol=" + rol +
                '}';
    }
}