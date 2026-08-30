
package dto;





public class AsignacionEmpleadoZonaDTO {
    private EmpleadoDTO empleado;
    private ZonaDTO zona;
    private int cantVehiculosACargo;

    public AsignacionEmpleadoZonaDTO(EmpleadoDTO empleado, ZonaDTO zona, int cantVehiculosACargo) {
        this.empleado = empleado;
        this.zona = zona;
        this.cantVehiculosACargo = cantVehiculosACargo;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public ZonaDTO getZona() {
        return zona;
    }

    public int getCantVehiculosACargo() {
        return cantVehiculosACargo;
    }

    public void setEmpleadoDTO(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public void setZonaDTO(ZonaDTO zona) {
        this.zona = zona;
    }

    public void setCantVehiculosACargo(int cantVehiculosACargo) {
        this.cantVehiculosACargo = cantVehiculosACargo;
    }
    
}