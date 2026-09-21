package controller;

import exceptions.ErrorNegocio;
import model.Rol;
import model.Usuario;
import service.AsignacionEmpleadoZonaService;

import java.util.regex.Pattern;

public class AsignacionEmpleadoZonaController implements Controlador {

    private final AsignacionEmpleadoZonaService asignacionService;
    private static final Pattern PATTERN_NUMERO = Pattern.compile("^[0-9]+$");

    public AsignacionEmpleadoZonaController() {
        this.asignacionService = new AsignacionEmpleadoZonaService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {}

    public String crearAsignacion(Usuario usuarioSesion, String idEmpleadoStr, String idZonaStr, String cantVehiculosStr) {
        try {
            validarAdministrador(usuarioSesion);

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

            // Delegación completa al servicio (que se encarga de buscar y validar las entidades)
            asignacionService.crearAsignacionPorIds(idEmpleado, idZona, cantVehiculos);
            return "Asignación creada exitosamente.";

        } catch (SecurityException e) {
            return "Acceso denegado: " + e.getMessage();
        } catch (ErrorNegocio e) {
            return "Error de negocio: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado: " + e.getMessage();
        }
    }

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