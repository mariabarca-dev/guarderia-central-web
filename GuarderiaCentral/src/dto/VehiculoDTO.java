package dto;

public class VehiculoDTO {
    
    private int id;
    private int socioId;
    private int empleadoId; // <--- CAMBIO 1: Añadido atributo para la relación con empleado
    private String nombre;
    private String matricula;
    private String tipo;
    private float profundidad;
    private float ancho;

    /**
     * Constructor actualizado para incluir empleadoId.
     */
    public VehiculoDTO(int id, int socioId, int empleadoId, String nombre, String matricula, String tipo, float profundidad, float ancho) {
        this.id = id;
        this.socioId = socioId;
        this.empleadoId = empleadoId; // <--- CAMBIO 2: Asignación en constructor
        this.nombre = nombre;
        this.matricula = matricula;
        this.tipo = tipo;
        this.profundidad = profundidad;
        this.ancho = ancho;
    }

    // --- GETTERS ---
    public int getId(){ return id; }
    public int getSocioId(){ return socioId; }
    /**
     * Retorna el ID del empleado responsable de este vehículo.
     * @return int empleadoId
     */
    public int getEmpleadoId(){ return empleadoId; } // <--- CAMBIO 3: Getter nuevo
    public String getNombre(){ return nombre; }
    public String getMatricula(){ return matricula; }
    public String getTipo(){ return tipo; }
    public float getProfundidad(){ return profundidad; }
    public float getAncho(){ return ancho; }
    
    // --- SETTERS ---
    public void setId(int id){ this.id = id; }
    public void setSocioId(int socioId) { this.socioId = socioId; }
    /**
     * Establece el ID del empleado responsable de este vehículo.
     * @param empleadoId El ID del empleado.
     */
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; } // <--- CAMBIO 4: Setter nuevo
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setProfundidad(float profundidad) { this.profundidad = profundidad; }
    public void setAncho(float ancho) { this.ancho = ancho; }
    
}