package view.impl;

import controller.EmpleadoController;
import dto.EmpleadoDTO;
import dto.ZonaDTO;
import dto.VehiculoDTO;
import java.util.List;

public class MenuEmpleadoImpl extends VistaImpl {
    private final EmpleadoController empleadoController;
    private final EmpleadoDTO empleadoLogueado;

    public MenuEmpleadoImpl(EmpleadoController empleadoController, EmpleadoDTO empleado) {
        this.empleadoController = empleadoController;
        this.empleadoLogueado = empleado;
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
            // Se mantiene el uso de List<ZonaDTO> ya que el controlador devuelve DTOs
            List<ZonaDTO> zonas = empleadoController.listarZonasAsignadas(empleadoLogueado.getId());
            
            if (zonas == null || zonas.isEmpty()) {
                System.out.println("No tiene zonas asignadas.");
            } else {
                imprimirEncabezado("--- Zonas asignadas ---");
                for (ZonaDTO z : zonas) {
                    // CORRECCIÓN: Se usan los métodos estándar de ZonaDTO (sin sufijo DTO)
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
            // Se mantiene el uso de List<VehiculoDTO> ya que el controlador devuelve DTOs
            List<VehiculoDTO> vehiculos = empleadoController.listarVehiculosBajoResponsabilidad(empleadoLogueado.getId());
            
            if (vehiculos == null || vehiculos.isEmpty()) {
                System.out.println("No tiene vehículos a cargo.");
            } else {
                imprimirEncabezado("--- Vehículos a cargo ---");
                for (VehiculoDTO v : vehiculos) {
                    // CORRECCIÓN: Se usan los métodos estándar de VehiculoDTO
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