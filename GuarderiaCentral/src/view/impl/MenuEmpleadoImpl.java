package view.impl;

import controller.EmpleadoController;
import dto.EmpleadoDTO;
import dto.ZonaDTO;
import dto.VehiculoDTO;
import model.Usuario; // <- Importante
import java.util.List;

import view.MenuEmpleado;

public class MenuEmpleadoImpl extends VistaImpl implements MenuEmpleado {
    private final EmpleadoController empleadoController;
    private final EmpleadoDTO empleadoLogueado;
    private final Usuario usuarioSesion; // <- Guardamos la sesión aquí

    // Modificamos el constructor para recibir también el Usuario de sesión
    public MenuEmpleadoImpl(EmpleadoController empleadoController, EmpleadoDTO empleado, Usuario usuarioSesion) {
        this.empleadoController = empleadoController;
        this.empleadoLogueado = empleado;
        this.usuarioSesion = usuarioSesion;
    }

    @Override
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            imprimirEncabezado("=== Menú Empleado ===");
            System.out.println("Empleado: " + empleadoLogueado.getNombre() + " (" + empleadoLogueado.getEspecialidad() + ")");
            System.out.println("1. Consultar mis Zonas Asignadas");
            System.out.println("2. Consultar Vehículos a mi Cargo");
            System.out.println("3. Cerrar Sesión");

            int opcion = leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    listarZonasAsignadas();
                    break;

                case 2:
                    listarVehiculosACargo();
                    break;

                case 3:
                    System.out.println("Sesión cerrada.");
                    salir = true;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void listarZonasAsignadas() {
        try {
            // CORRECCIÓN: Le pasamos 'usuarioSesion' como primer argumento y luego el ID
            List<ZonaDTO> zonas = empleadoController.listarZonasAsignadas(this.usuarioSesion, empleadoLogueado.getId());

            if (zonas == null || zonas.isEmpty()) {
                System.out.println("No tiene zonas asignadas.");
            } else {
                imprimirEncabezado("--- Zonas asignadas ---");
                for (ZonaDTO z : zonas) {
                    System.out.println("Zona: " + z.getLetra() +
                            " - Capacidad: " + z.getCapacidadVehiculos());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener zonas: " + e.getMessage());
        }
        presionarParaContinuar();
    }

    private void listarVehiculosACargo() {
        try {
            // CORRECCIÓN: Le pasamos 'usuarioSesion' como primer argumento y luego el ID
            List<VehiculoDTO> vehiculos = empleadoController.listarVehiculosBajoResponsabilidad(this.usuarioSesion, empleadoLogueado.getId());

            if (vehiculos == null || vehiculos.isEmpty()) {
                System.out.println("No tiene vehículos a cargo.");
            } else {
                imprimirEncabezado("--- Vehículos a cargo ---");
                for (VehiculoDTO v : vehiculos) {
                    System.out.println("ID: " + v.getId() +
                            " - Matrícula: " + v.getMatricula() +
                            " - Tipo: " + v.getTipo());
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener vehículos: " + e.getMessage());
        }
        presionarParaContinuar();
    }
}