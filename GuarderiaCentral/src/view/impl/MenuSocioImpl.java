
package view.impl;



import controller.SocioController;
import dto.SocioDTO;
import dto.VehiculoDTO;
import dto.GarageDTO;
import java.util.List;

public class MenuSocioImpl extends VistaImpl {
    private final SocioController socioController;
    private final SocioDTO socioLogueado;

    public MenuSocioImpl(SocioController socioController, SocioDTO socio) {
        this.socioController = socioController;
        this.socioLogueado = socio;
    }

    @Override
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("=== Menú Socio ===");
            System.out.println("Socio: " + socioLogueado.getNombre() + " (DNI: " + socioLogueado.getDni() + ")");
            System.out.println("1. Mis Datos Personales");
            System.out.println("2. Ver mis Vehículos");
            System.out.println("3. Ver mis Garajes Comprados/Alquilados");
            System.out.println("4. Cerrar Sesión");

            int opcion = leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    // Mostrar datos personales
                    SocioDTO datos = socioController.buscarSocioPorId(socioLogueado.getId());
                    if (datos != null) {
                        System.out.println("--- Datos Personales ---");
                        System.out.println("Nombre: " + datos.getNombre());
                        System.out.println("Usuario: " + datos.getNombreUsuario());
                        System.out.println("Dirección: " + datos.getDireccion());
                        System.out.println("Teléfono: " + datos.getTelefono());
                        System.out.println("DNI: " + datos.getDni());
                        System.out.println("Fecha de Ingreso: " + datos.getFechaIngreso());
                    } else {
                        System.out.println("Error: no se encontraron datos del socio.");
                    }
                    break;

                case 2:
                    // Listar vehículos del socio
                    List<VehiculoDTO> vehiculos = socioController.listarVehiculosPorSocio(socioLogueado.getId());
                    if (vehiculos.isEmpty()) {
                        System.out.println("No tiene vehículos registrados.");
                    } else {
                        System.out.println("--- Vehículos ---");
                        for (VehiculoDTO v : vehiculos) {
                            System.out.println("ID: " + v.getId() + " - Matrícula: " + v.getMatricula() + " - Tipo: " + v.getTipo());
                        }
                    }
                    break;

                case 3:
                    // Listar garajes del socio
                    List<GarageDTO> garajes = socioController.listarGarajesPorSocio(socioLogueado.getId());
                    if (garajes.isEmpty()) {
                        System.out.println("No tiene garajes comprados o alquilados.");
                    } else {
                        System.out.println("--- Garajes ---");
                        for (GarageDTO g : garajes) {
                            System.out.println("ID: " + g.getId() + " - Zona: " + g.getZona() + " - Fecha Compra: " + g.getFechaCompra());
                        }
                    }
                    break;

                case 4:
                    System.out.println("Sesión cerrada.");
                    salir = true;
                    break;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}

