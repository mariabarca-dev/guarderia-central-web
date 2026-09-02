package controller;

import dto.AsignacionEmpleadoZonaDTO;
import dto.EmpleadoDTO;
import dto.ZonaDTO;
import exceptions.ErrorNegocio;
import service.AsignacionEmpleadoZonaService;
import service.EmpleadoService;
import service.ZonaService;

public class AsignacionEmpleadoZonaController implements Controlador {

    @Override
    public void login(String nombreUsuario, String claveIngresada) {

    }

    private final AsignacionEmpleadoZonaService asignacionService;
    private final EmpleadoService empleadoService;
    private final ZonaService zonaService;

    public AsignacionEmpleadoZonaController() {
        this.asignacionService = new AsignacionEmpleadoZonaService();
        this.empleadoService = new EmpleadoService();
        this.zonaService = new ZonaService();
    }

    /**
     * Recibe los datos desde la vista en formato String y valida sintaxis y formato.
     */
    public String crearAsignacion(String idEmpleadoStr, String idZonaStr, String cantVehiculosStr) {

        // --- 1. VALIDACIONES DE SINTAXIS Y FORMATO (Controller) ---
        if (idEmpleadoStr == null || idEmpleadoStr.isBlank() ||
                idZonaStr == null || idZonaStr.isBlank() ||
                cantVehiculosStr == null || cantVehiculosStr.isBlank()) {
            return "Error de sintaxis: Todos los campos son obligatorios.";
        }

        int idEmpleado, idZona, cantVehiculos;
        try {
            idEmpleado = Integer.parseInt(idEmpleadoStr.trim());
            idZona = Integer.parseInt(idZonaStr.trim());
            cantVehiculos = Integer.parseInt(cantVehiculosStr.trim());
        } catch (NumberFormatException e) {
            return "Error de formato: Los IDs y la cantidad de vehículos deben ser números enteros válidos.";
        }

        if (idEmpleado <= 0 || idZona <= 0) {
            return "Error de formato: Los IDs deben ser mayores a cero.";
        }

        if (cantVehiculos < 0) {
            return "Error de formato: La cantidad de vehículos no puede ser un número negativo.";
        }

        // --- 2. ARMADO DEL DTO Y LLAMADA AL SERVICE (Reglas de Negocio) ---
        try {
            // Buscamos los DTOs correspondientes para armar la asignación
            EmpleadoDTO empleadoDto = empleadoService.listarTodos().stream()
                    .filter(e -> e.getId() == idEmpleado)
                    .findFirst()
                    .orElse(null);

            ZonaDTO zonaDto = zonaService.listarTodas().stream()
                    .filter(z -> z.getId() == idZona)
                    .findFirst()
                    .orElse(null);

            if (empleadoDto == null || zonaDto == null) {
                return "Error de negocio: El empleado o la zona especificada no existen.";
            }

            AsignacionEmpleadoZonaDTO dto = new AsignacionEmpleadoZonaDTO(empleadoDto, zonaDto, cantVehiculos);

            // Delegamos la lógica de negocio al Service
            asignacionService.crearAsignacion(dto);
            return "Asignación creada exitosamente.";

        } catch (ErrorNegocio e) {
            // Atrapamos la excepción de negocio lanzada por el Service (ej. ZonaSinCapacidadException)
            return "Error de negocio: " + e.getMessage();
        }
    }

}
