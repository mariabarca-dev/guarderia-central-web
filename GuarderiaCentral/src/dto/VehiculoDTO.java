package dto;

public class VehiculoDTO {

    private int id;
    private int socioId;
    private int empleadoId;
    private String nombre;
    private String matricula;
    private String tipo;
    private float profundidad;
    private float ancho;

    /**
     * Constructor vacío requerido para instanciación por setters (ej. en vistas y mappers).
     */
    public VehiculoDTO() {
    }


    /**
     * Constructor completo con todos los atributos.
     */
    public VehiculoDTO(int id, int socioId, int empleadoId, String nombre, String matricula, String tipo, float profundidad, float ancho) {
        this.id = id;
        this.socioId = socioId;
        this.empleadoId = empleadoId;
        this.nombre = nombre;
        this.matricula = matricula;
        this.tipo = tipo;
        this.profundidad = profundidad;
        this.ancho = ancho;
    }

    // --- GETTERS ---
    public int getId() { return id; }
    public int getSocioId() { return socioId; }
    public int getEmpleadoId() { return empleadoId; }
    public String getNombre() { return nombre; }
    public String getMatricula() { return matricula; }
    public String getTipo() { return tipo; }
    public float getProfundidad() { return profundidad; }
    public float getAncho() { return ancho; }

    // --- GETTERS Y SETTERS COMPATIBLES CON LA VISTA ---
    public String getMarca() { return nombre; }
    public void setMarca(String marca) { this.nombre = marca; }

    public String getModelo() { return tipo; }
    public void setModelo(String modelo) { this.tipo = modelo; }

    // --- SETTERS ---
    public void setId(int id) { this.id = id; }
    public void setSocioId(int socioId) { this.socioId = socioId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setProfundidad(float profundidad) { this.profundidad = profundidad; }
    public void setAncho(float ancho) { this.ancho = ancho; }
}