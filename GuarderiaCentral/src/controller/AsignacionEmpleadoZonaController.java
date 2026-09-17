package controller;

import dto.AsignacionEmpleadoZonaDTO;
import dto.EmpleadoDTO;
import dto.ZonaDTO;
import exceptions.ErrorNegocio;
import model.Rol;
import model.Usuario;
import service.AsignacionEmpleadoZonaService;
import service.EmpleadoService;
import service.ZonaService;

import java.util.regex.Pattern;

public class AsignacionEmpleadoZonaController implements Controlador {

    private final AsignacionEmpleadoZonaService asignacionService;
    private final EmpleadoService empleadoService;
    private final ZonaService zonaService;

    // Patrón de validación de sintaxis y formato para números enteros
    private static final Pattern PATTERN_NUMERO = Pattern.compile("^[0-9]+$");

    public AsignacionEmpleadoZonaController() {
        this.asignacionService = new AsignacionEmpleadoZonaService();
        this.empleadoService = new EmpleadoService();
        this.zonaService = new ZonaService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        // Los controladores específicos no manejan el login directamente
    }

    /**
     * Crea una asignación de empleado a zona validando el rol, la sesión y el formato mediante patrones.
     */
    public String crearAsignacion(Usuario usuarioSesion, String idEmpleadoStr, String idZonaStr, String cantVehiculosStr) {
        try {
            validarAdministrador(usuarioSesion);

            // --- 1. VALIDACIONES DE SINTAXIS Y FORMATO (Controller con Pattern) ---
            if (idEmpleadoStr == null || idEmpleadoStr.isBlank() ||
                    idZonaStr == null || idZonaStr.isBlank() ||
                    cantVehiculosStr == null || cantVehiculosStr.isBlank()) {
                return "Error de sintaxis: Todos los campos son obligatorios.";
            }

            if (!PATTERN_NUMERO.matcher(idEmpleadoStr.trim()).matches() ||
                    !PATTERN_NUMERO.matcher(idZonaStr.trim()).matches() ||
                    !PATTERN_NUMERO.matcher(cantVehiculosStr.trim()).matches()) {
                return "Error de formato: Los IDs y la cantidad de vehículos deben ser números enteros válidos.";
            }

            int idEmpleado = Integer.parseInt(idEmpleadoStr.trim());
            int idZona = Integer.parseInt(idZonaStr.trim());
            int cantVehiculos = Integer.parseInt(cantVehiculosStr.trim());

            if (idEmpleado <= 0 || idZona <= 0) {
                return "Error de formato: Los IDs deben ser mayores a cero.";
            }

            if (cantVehiculos < 0) {
                return "Error de formato: La cantidad de vehículos no puede ser un número negativo.";
            }

            // --- 2. ARMADO DEL DTO Y LLAMADA AL SERVICE (Reglas de Negocio) ---
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

        } catch (SecurityException e) {
            return "Acceso denegado: " + e.getMessage();
        } catch (ErrorNegocio e) {
            return "Error de negocio: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }

    // --- Métodos Privados de Validación de Sesión y Seguridad ---

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarAdministrador(Usuario usuario) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            throw new SecurityException("Se requieren permisos de ADMINISTRADOR.");
        }
    }
}