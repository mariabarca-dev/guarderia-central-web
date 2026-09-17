package view.impl;

import controller.AdminController;
import controller.AsignacionEmpleadoZonaController;
import controller.AsignacionVehiculoGarageController;
import controller.EmpleadoController;
import controller.GarageController;
import controller.PropiedadGarageController;
import controller.SocioController;
import controller.VehiculoController;
import controller.ZonaController;
import dto.*;
import exceptions.ErrorNegocio;
import exceptions.GarageYaOcupadoException;
import exceptions.GarageYaVendidoException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Usuario;

public class MenuAdminImpl extends VistaImpl {

    private static final int LIMITE_DNI = 8;
    private static final int LIMITE_TELEFONO = 10;
    private static final int LIMITE_CODIGO = 3;

    // --- CONTROLADORES ESPECÍFICOS POR ENTIDAD Y RELACIÓN ---
    private final AdminController adminController;
    private final AsignacionEmpleadoZonaController asignacionEmpleadoZonaController;
    private final AsignacionVehiculoGarageController asignacionVehiculoGarageController;
    private final EmpleadoController empleadoController;
    private final SocioController socioController;
    private final VehiculoController vehiculoController;
    private final GarageController garageController;
    private final PropiedadGarageController propiedadGarageController;
    private final ZonaController zonaController;

    private final Usuario usuarioSesion;
    private final Scanner scanner = new Scanner(System.in);

    public MenuAdminImpl(AdminController adminController,
                         AsignacionEmpleadoZonaController asignacionEmpleadoZonaController,
                         AsignacionVehiculoGarageController asignacionVehiculoGarageController,
                         EmpleadoController empleadoController,
                         SocioController socioController,
                         VehiculoController vehiculoController,
                         GarageController garageController,
                         PropiedadGarageController propiedadGarageController,
                         ZonaController zonaController,
                         Usuario usuarioSesion) {
        this.adminController = adminController;
        this.asignacionEmpleadoZonaController = asignacionEmpleadoZonaController;
        this.asignacionVehiculoGarageController = asignacionVehiculoGarageController;
        this.empleadoController = empleadoController;
        this.socioController = socioController;
        this.vehiculoController = vehiculoController;
        this.garageController = garageController;
        this.propiedadGarageController = propiedadGarageController;
        this.zonaController = zonaController;
        this.usuarioSesion = usuarioSesion;
    }

    @Override
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Panel de Administración Central ---");
            System.out.println("1. ABM Entidades (Socios, Empleados, Vehículos, Garajes, Zonas)");
            System.out.println("2. Registrar Venta de Garaje a Socio");
            System.out.println("3. Asignar Vehículo a Garaje");
            System.out.println("4. Asignar Empleado a Zona");
            System.out.println("5. Consultas de Ocupación y Disponibilidad");
            System.out.println("6. Cerrar Sesión");

            int opcion = leerEntero("Seleccione una opción");
            switch (opcion) {
                case 1:
                    try {
                        mostrarSubmenuCRUD();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 2:
                    try {
                        ejecutarVentaGarage();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 3:
                    try {
                        ejecutarAsignacionVehiculo();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 4:
                    try {
                        ejecutarAsignacionEmpleado();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case 5:
                    mostrarSubmenuConsultas();
                    break;
                case 6:
                    System.out.println("Sesión de administrador finalizada.");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void mostrarSubmenuCRUD() throws ErrorNegocio {
        System.out.println("\n--- GESTIÓN DE ENTIDADES (CRUD) ---");
        System.out.println("1. Alta Socio");
        System.out.println("2. Modificar Socio");
        System.out.println("3. Baja Socio");
        System.out.println("4. Alta Empleado");
        System.out.println("5. Modificar Empleado");
        System.out.println("6. Baja Empleado");
        System.out.println("7. Alta Vehículo");
        System.out.println("8. Modificar Vehículo");
        System.out.println("9. Baja Vehículo");
        System.out.println("10. Alta Garaje");
        System.out.println("11. Modificar Garaje");
        System.out.println("12. Baja Garaje");
        System.out.println("13. Alta Zona");
        System.out.println("14. Modificar Zona");
        System.out.println("15. Baja Zona");
        System.out.println("16. Volver");

        int op = leerEntero("Seleccione qué desea gestionar");
        switch (op) {
            case 1:
                altaUsuario("socio");
                break;
            case 2:
                int idSocio = leerNumeroPositivo("Ingrese el ID del socio a modificar");
                modificarUsuario(idSocio, "socio");
                break;
            case 3:
                eliminarUsuario("socio");
                break;
            case 4:
                altaUsuario("empleado");
                break;
            case 5:
                int idEmpleado = leerNumeroPositivo("Ingrese el ID del empleado a modificar");
                modificarUsuario(idEmpleado, "empleado");
                break;
            case 6:
                eliminarUsuario("empleado");
                break;
            case 7:
                altaVehiculo();
                break;
            case 8:
                int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a modificar");
                modificarVehiculo(idVehiculo);
                break;
            case 9:
                eliminarVehiculo();
                break;
            case 10:
                altaGaraje();
                break;
            case 11:
                int idGaraje = leerNumeroPositivo("Ingrese el ID del garaje a modificar");
                modificarGarage(idGaraje);
                break;
            case 12:
                eliminarGarage();
                break;
            case 13:
                altaZona();
                break;
            case 14:
                int idZona = leerNumeroPositivo("Ingrese el ID de la zona a modificar");
                modificarZona(idZona);
                break;
            case 15:
                eliminarZona();
                break;
            case 16:
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void ejecutarVentaGarage() throws ErrorNegocio {
        System.out.println("Operación: Propiedad de Garaje");

        int socioId = leerNumeroPositivo("ID del Socio comprador");
        SocioDTO socio = socioController.buscarSocioPorId(socioId);
        if (socio == null) {
            System.out.println("Error: no existe un socio con ese ID.");
            return;
        }

        GarageDTO garage = null;
        boolean garageValido = false;

        do {
            int garageId = leerNumeroPositivo("ID del Garaje");
            garage = garageController.buscarGaragePorId(garageId);

            if (garage == null) {
                System.out.println("Error: no existe un garaje con ese ID.");
            } else {
                try {
                    if (garage.getSocioPropietario() != null) {
                        throw new GarageYaVendidoException();
                    }
                    garageValido = true;
                } catch (GarageYaVendidoException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (!garageValido);

        try {
            LocalDate fechaCompra = leerFechaValida("Fecha de Compra (YYYY-MM-DD)", false);
            PropiedadGarageDTO prop = new PropiedadGarageDTO(socio, garage, fechaCompra);
            propiedadGarageController.asignarPropiedadGarage(prop);
            System.out.println("Propiedad de garaje registrada correctamente.");
        } catch (ErrorNegocio e) {
            System.out.println(e.getMessage());
        }
    }

    private void ejecutarAsignacionVehiculo() throws ErrorNegocio {
        System.out.println("Operación: Ocupación de Garaje");

        int vehiculoId = leerNumeroPositivo("ID del Vehículo");
        VehiculoDTO vehiculo = vehiculoController.buscarVehiculoPorId(vehiculoId);
        if (vehiculo == null) {
            System.out.println("Error: no existe un vehículo con ese ID.");
            return;
        }

        GarageDTO garage = null;
        boolean garageValido = false;

        do {
            int garajeId = leerNumeroPositivo("ID del Garaje a ocupar (0 para cancelar)");

            if (garajeId == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            garage = garageController.buscarGaragePorId(garajeId);

            if (garage == null) {
                System.out.println("Error: no existe un garaje con ese ID.");
            } else {
                try {
                    AsignacionVehiculoGarageDTO asignacion = asignacionVehiculoGarageController.buscarAsignacionPorGarage(garajeId);
                    if (asignacion != null) {
                        throw new GarageYaOcupadoException();
                    }
                    garageValido = true;
                } catch (GarageYaOcupadoException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (!garageValido);

        LocalDate fechaAsignacion = leerFechaValida("Fecha de Asignación (YYYY-MM-DD)", false);
        AsignacionVehiculoGarageDTO nuevaAsignacion = new AsignacionVehiculoGarageDTO(vehiculo, garage, fechaAsignacion);
        asignacionVehiculoGarageController.asignarVehiculoAGarageDTO(nuevaAsignacion);

        System.out.println("Vehículo asignado correctamente al garaje.");
    }

    private void ejecutarAsignacionEmpleado() throws ErrorNegocio {
        System.out.println("Operación: Carga de Personal en Zona");

        int empleadoId = leerNumeroPositivo("ID del Empleado");
        EmpleadoDTO empleado = empleadoController.buscarEmpleadoPorId(empleadoId);
        if (empleado == null) {
            System.out.println("Error: no existe un empleado con ese ID.");
            return;
        }

        int zonaId = leerNumeroPositivo("ID de la Zona");
        ZonaDTO zona = zonaController.buscarZonaPorId(zonaId);
        if (zona == null) {
            System.out.println("Error: no existe una zona con ese ID.");
            return;
        }

        int vehiculosACargo = leerNumeroPositivo("Cantidad de vehículos bajo su cargo");

        List<VehiculoDTO> vehiculosEnZona = vehiculoController.listarVehiculosPorZona(zonaId);
        int cantidadReal = (vehiculosEnZona != null) ? vehiculosEnZona.size() : 0;

        if (cantidadReal < vehiculosACargo) {
            System.out.println("Error: la zona solo tiene " + cantidadReal + " vehículos.");
            return;
        }

        System.out.println("Vehículos disponibles en la zona:");
        for (VehiculoDTO v : vehiculosEnZona) {
            System.out.println("ID: " + v.getId() + " - Matrícula: " + v.getMatricula());
        }

        List<Integer> idsSeleccionados = new ArrayList<>();
        for (int i = 0; i < vehiculosACargo; i++) {
            int idVehiculo = leerNumeroPositivo("Ingrese ID del vehículo #" + (i + 1) + " a cargo");
            idsSeleccionados.add(idVehiculo);
        }

        AsignacionEmpleadoZonaDTO asignacion = new AsignacionEmpleadoZonaDTO(empleado, zona, vehiculosACargo);
        asignacionEmpleadoZonaController.asignarEmpleadoAZona(asignacion);

        System.out.println("Empleado asignado correctamente a la zona.");
    }

    private void mostrarSubmenuConsultas() {
        System.out.println("\n--- REPORTES Y CONSULTAS ---");
        System.out.println("1. Ver Disponibilidad y Ocupación General de Garajes");
        System.out.println("2. Consultar Vehículos por Zona");
        System.out.println("3. Consultar Empleados por Zona");

        int op = leerEntero("Seleccione consulta");
        switch (op) {
            case 1:
                garageController.listarDisponibilidadGarages();
                break;
            case 2:
                int idZ = leerNumeroPositivo("Ingrese ID de la Zona");
                vehiculoController.listarVehiculosPorZona(idZ);
                break;
            case 3:
                int idZonaEmp = leerNumeroPositivo("Ingrese ID de la Zona");
                empleadoController.listarEmpleadosPorZona(idZonaEmp);
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ---------------- OPERACIONES DE ENTIDADES (CRUD) ----------------

    private void altaUsuario(String tipo) {
        String dni = leerTextoConLimite("DNI", LIMITE_DNI);
        String nombre = leerTexto("Nombre");
        String apellido = leerTexto("Apellido");
        String direccion = leerTexto("Dirección");
        String telefono = leerTextoConLimite("Teléfono", LIMITE_TELEFONO);

        if ("socio".equalsIgnoreCase(tipo)) {
            SocioDTO dto = new SocioDTO();
            dto.setDni(dni);
            dto.setNombre(nombre);
            dto.setApellido(apellido);
            dto.setDireccion(direccion);
            dto.setTelefono(telefono);
            socioController.registrarSocio(dto);
            System.out.println("Socio registrado con éxito.");
        } else if ("empleado".equalsIgnoreCase(tipo)) {
            String codigo = leerTextoConLimite("Código de Empleado", LIMITE_CODIGO);
            EmpleadoDTO dto = new EmpleadoDTO();
            dto.setDni(dni);
            dto.setNombre(nombre);
            dto.setApellido(apellido);
            dto.setDireccion(direccion);
            dto.setTelefono(telefono);
            dto.setCodigoEmpleado(codigo);
            empleadoController.registrarEmpleado(dto);
            System.out.println("Empleado registrado con éxito.");
        }
    }

    private void modificarUsuario(int id, String tipo) {
        if ("socio".equalsIgnoreCase(tipo)) {
            SocioDTO socio = socioController.buscarSocioPorId(id);
            if (socio == null) {
                System.out.println("No existe el socio especificado.");
                return;
            }
            socio.setNombre(leerTexto("Nuevo Nombre (actual: " + socio.getNombre() + ")"));
            socio.setApellido(leerTexto("Nuevo Apellido (actual: " + socio.getApellido() + ")"));
            socio.setDireccion(leerTexto("Nueva Dirección (actual: " + socio.getDireccion() + ")"));
            socio.setTelefono(leerTextoConLimite("Nuevo Teléfono", LIMITE_TELEFONO));
            socioController.modificarSocio(socio);
            System.out.println("Socio modificado exitosamente.");
        } else if ("empleado".equalsIgnoreCase(tipo)) {
            EmpleadoDTO empleado = empleadoController.buscarEmpleadoPorId(id);
            if (empleado == null) {
                System.out.println("No existe el empleado especificado.");
                return;
            }
            empleado.setNombre(leerTexto("Nuevo Nombre (actual: " + empleado.getNombre() + ")"));
            empleado.setApellido(leerTexto("Nuevo Apellido (actual: " + empleado.getApellido() + ")"));
            empleado.setDireccion(leerTexto("Nueva Dirección (actual: " + empleado.getDireccion() + ")"));
            empleado.setTelefono(leerTextoConLimite("Nuevo Teléfono", LIMITE_TELEFONO));
            empleadoController.modificarEmpleado(empleado);
            System.out.println("Empleado modificado exitosamente.");
        }
    }

    private void eliminarUsuario(String tipo) {
        int id = leerNumeroPositivo("Ingrese ID a eliminar");
        if ("socio".equalsIgnoreCase(tipo)) {
            socioController.eliminarSocio(id);
            System.out.println("Socio eliminado con éxito.");
        } else if ("empleado".equalsIgnoreCase(tipo)) {
            empleadoController.eliminarEmpleado(id);
            System.out.println("Empleado eliminado con éxito.");
        }
    }

    private void altaVehiculo() {
        String matricula = leerTexto("Matrícula");
        String marca = leerTexto("Marca");
        String modelo = leerTexto("Modelo");
        VehiculoDTO dto = new VehiculoDTO();
        dto.setMatricula(matricula);
        dto.setMarca(marca);
        dto.setModelo(modelo);
        vehiculoController.registrarVehiculo(dto);
        System.out.println("Vehículo registrado correctamente.");
    }

    private void modificarVehiculo(int idVehiculo) {
        VehiculoDTO v = vehiculoController.buscarVehiculoPorId(idVehiculo);
        if (v == null) {
            System.out.println("Vehículo no encontrado.");
            return;
        }
        v.setMarca(leerTexto("Nueva Marca (actual: " + v.getMarca() + ")"));
        v.setModelo(leerTexto("Nuevo Modelo (actual: " + v.getModelo() + ")"));
        vehiculoController.modificarVehiculo(v);
        System.out.println("Vehículo modificado con éxito.");
    }

    private void eliminarVehiculo() throws ErrorNegocio {
        System.out.println("--- Lista de Vehículos ---");
        vehiculoController.listarTodosLosVehiculos();

        int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a eliminar");
        VehiculoDTO vehiculo = vehiculoController.buscarVehiculoPorId(idVehiculo);

        if (vehiculo == null) {
            System.out.println("No se encontró ningún vehículo con ese ID.");
            return;
        }

        vehiculoController.eliminarVehiculo(idVehiculo);
        System.out.println("Vehículo eliminado correctamente.");
    }

    private void altaGaraje() {
        int numero = leerNumeroPositivo("Número de Garaje");
        GarageDTO dto = new GarageDTO();
        dto.setNumero(numero);
        garageController.registrarGarage(dto);
        System.out.println("Garaje registrado correctamente.");
    }

    private void modificarGarage(int id) {
        GarageDTO g = garageController.buscarGaragePorId(id);
        if (g == null) {
            System.out.println("Garaje no encontrado.");
            return;
        }
        g.setNumero(leerNumeroPositivo("Nuevo Número de Garaje (actual: " + g.getNumero() + ")"));
        garageController.modificarGarage(g);
        System.out.println("Garaje modificado correctamente.");
    }

    private void eliminarGarage() {
        int id = leerNumeroPositivo("ID del Garaje a eliminar");
        garageController.eliminarGarage(id);
        System.out.println("Garaje eliminado correctamente.");
    }

    private void altaZona() {
        String letra = leerTexto("Letra/Identificador de la Zona");
        int capacidad = leerNumeroPositivo("Capacidad máxima de la zona");
        ZonaDTO dto = new ZonaDTO();
        dto.setLetra(letra);
        dto.setCapacidad(capacidad);
        zonaController.registrarZona(usuarioSesion, dto);
        System.out.println("Zona registrada con éxito.");
    }

    private void modificarZona(int id) {
        ZonaDTO z = zonaController.buscarZonaPorId(id);
        if (z == null) {
            System.out.println("Zona no encontrada.");
            return;
        }
        z.setCapacidad(leerNumeroPositivo("Nueva Capacidad (actual: " + z.getCapacidad() + ")"));
        zonaController.actualizarZona(usuarioSesion, z);
        System.out.println("Zona actualizada exitosamente.");
    }

    private void eliminarZona() {
        int id = leerNumeroPositivo("ID de la Zona a eliminar");
        zonaController.eliminarZona(usuarioSesion, id);
        System.out.println("Zona eliminada correctamente.");
    }

    // ---------------- MÉTODOS REUTILIZABLES DE LECTURA ----------------

    @Override
    protected String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }

    private String leerTextoConLimite(String mensaje, int limite) {
        String texto;
        do {
            System.out.print(mensaje + " (máx " + limite + " caracteres): ");
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Error: el texto no puede estar vacío.");
            } else if (texto.length() > limite) {
                System.out.println("Error: supera el límite de " + limite + " caracteres.");
            }
        } while (texto.length() > limite || texto.isEmpty());
        return texto;
    }

    @Override
    protected int leerEntero(String mensaje) {
        int numero = 0;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print(mensaje + ": ");
                numero = Integer.parseInt(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: formato inválido, ingrese un número entero.");
            }
        }
        return numero;
    }

    private int leerNumeroPositivo(String mensaje) {
        int num;
        do {
            num = leerEntero(mensaje);
            if (num < 0) {
                System.out.println("Error: ingrese un número mayor o igual a 0.");
            }
        } while (num < 0);
        return num;
    }

    private LocalDate leerFechaValida(String mensaje, boolean permiteFutura) {
        LocalDate fecha = null;
        boolean valida = false;
        while (!valida) {
            String textoFecha = leerTexto(mensaje);
            try {
                fecha = LocalDate.parse(textoFecha);
                if (!permiteFutura && fecha.isAfter(LocalDate.now())) {
                    System.out.println("Error: la fecha no puede ser futura.");
                } else {
                    valida = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: formato de fecha inválido. Utilice el formato YYYY-MM-DD.");
            }
        }
        return fecha;
    }
}