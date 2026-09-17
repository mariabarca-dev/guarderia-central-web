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
import exceptions.RegistroNoEncontradoException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                    ejecutarAsignacionEmpleado();
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
        System.out.println("5. Alta Vehículo");
        System.out.println("6. Modificar Vehículo");
        System.out.println("7. Baja Vehículo");
        System.out.println("8. Alta Garaje");
        System.out.println("9. Modificar Garaje");
        System.out.println("10. Baja Garaje");
        System.out.println("11. Alta Zona");
        System.out.println("12. Modificar Zona");
        System.out.println("13. Baja Zona");
        System.out.println("14. Volver");

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
                altaVehiculo();
                break;
            case 6:
                int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a modificar");
                modificarVehiculo(idVehiculo);
                break;
            case 7:
                eliminarVehiculo();
                break;
            case 8:
                altaGaraje();
                break;
            case 9:
                int numGaraje = leerNumeroPositivo("Ingrese el Número del garaje a modificar");
                modificarGarage(numGaraje);
                break;
            case 10:
                eliminarGarage();
                break;
            case 11:
                altaZona();
                break;
            case 12:
                String letraZonaMod = leerTexto("Ingrese la Letra de la zona a modificar");
                modificarZona(letraZonaMod);
                break;
            case 13:
                eliminarZona();
                break;
            case 14:
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

        int numeroGarage = leerNumeroPositivo("Número del Garaje");
        GarageDTO garage;
        try {
            garage = garageController.buscarPorNumero(usuarioSesion, numeroGarage);
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (garage.getSocioPropietario() != null) {
            System.out.println("Error: El garaje ya posee un propietario asociado.");
            return;
        }

        try {
            LocalDate fechaCompra = leerFechaValida("Fecha de Compra (YYYY-MM-DD)", false);
            PropiedadGarageDTO prop = new PropiedadGarageDTO(socio, garage, fechaCompra);
            propiedadGarageController.registrarPropiedad(usuarioSesion, prop);
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

        int numeroGarage = leerNumeroPositivo("Número del Garaje a ocupar (0 para cancelar)");
        if (numeroGarage == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        GarageDTO garage;
        try {
            garage = garageController.buscarPorNumero(usuarioSesion, numeroGarage);
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        LocalDate fechaAsignacion = leerFechaValida("Fecha de Asignación (YYYY-MM-DD)", false);
        AsignacionVehiculoGarageDTO nuevaAsignacion = new AsignacionVehiculoGarageDTO(vehiculo, garage, fechaAsignacion);

        asignacionVehiculoGarageController.crearAsignacion(usuarioSesion, nuevaAsignacion);
        System.out.println("Vehículo asignado correctamente al garaje.");
    }

    private void ejecutarAsignacionEmpleado() {
        System.out.println("Operación: Carga de Personal en Zona");

        String idEmpleadoStr = String.valueOf(leerNumeroPositivo("ID del Empleado"));
        String idZonaStr = String.valueOf(leerNumeroPositivo("ID de la Zona"));
        String cantVehiculosStr = String.valueOf(leerNumeroPositivo("Cantidad de vehículos bajo su cargo"));

        String resultado = asignacionEmpleadoZonaController.crearAsignacion(idEmpleadoStr, idZonaStr, cantVehiculosStr);
        System.out.println(resultado);
    }

    private void mostrarSubmenuConsultas() {
        System.out.println("\n--- REPORTES Y CONSULTAS ---");
        System.out.println("1. Ver Disponibilidad y Ocupación General de Garajes");
        System.out.println("2. Consultar Vehículos por Zona");
        System.out.println("3. Consultar Empleados por Zona");

        int op = leerEntero("Seleccione consulta");
        switch (op) {
            case 1:
                List<String> reporteDisponibilidad = garageController.consultarDisponibilidadGarages(usuarioSesion);
                if (reporteDisponibilidad != null && !reporteDisponibilidad.isEmpty()) {
                    reporteDisponibilidad.forEach(System.out.println);
                } else {
                    System.out.println("No se obtuvieron datos de disponibilidad.");
                }
                break;
            case 2:
                int idZ = leerNumeroPositivo("Ingrese ID de la Zona");
                vehiculoController.listarVehiculosPorZona(idZ);
                break;
            case 3:
                int idZonaEmp = leerNumeroPositivo("Ingrese ID de la Zona");
                List<ZonaDTO> zonasEmpleado = empleadoController.listarZonasAsignadas(idZonaEmp);
                System.out.println("Zonas asignadas al ID: " + zonasEmpleado.size());
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ---------------- OPERACIONES DE ENTIDADES (CRUD) ----------------

    private void altaUsuario(String tipo) {
        if ("socio".equalsIgnoreCase(tipo)) {
            String dni = leerTextoConLimite("DNI", LIMITE_DNI);
            String nombre = leerTexto("Nombre");
            String apellido = leerTexto("Apellido");
            String direccion = leerTexto("Dirección");
            String telefono = leerTextoConLimite("Teléfono", LIMITE_TELEFONO);

            SocioDTO dto = new SocioDTO();
            dto.setDni(dni);
            dto.setNombre(nombre);
            dto.setApellido(apellido);
            dto.setDireccion(direccion);
            dto.setTelefono(telefono);
            socioController.registrarSocio(dto);
            System.out.println("Socio registrado con éxito.");
        } else if ("empleado".equalsIgnoreCase(tipo)) {
            String nombre = leerTexto("Nombre");
            String direccion = leerTexto("Dirección");
            String telefono = leerTextoConLimite("Teléfono", LIMITE_TELEFONO);
            String nombreUsuario = leerTexto("Nombre de Usuario");
            String clave = leerTexto("Clave");
            String rolStr = leerTexto("Rol (ADMINISTRADOR / EMPLEADO)");
            String codigo = leerTextoConLimite("Código de Empleado", LIMITE_CODIGO);
            String especialidad = leerTexto("Especialidad");

            String respuesta = empleadoController.registrarEmpleado(nombre, direccion, telefono, nombreUsuario, clave, rolStr, codigo, especialidad);
            System.out.println(respuesta);
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
        } else {
            System.out.println("Operación no admitida para este tipo de usuario.");
        }
    }

    private void eliminarUsuario(String tipo) {
        int id = leerNumeroPositivo("Ingrese ID a eliminar");
        if ("socio".equalsIgnoreCase(tipo)) {
            socioController.eliminarSocio(id);
            System.out.println("Socio eliminado con éxito.");
        } else {
            System.out.println("Operación no soportada para empleados.");
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

    private void altaGaraje() throws ErrorNegocio {
        int numero = leerNumeroPositivo("Número de Garaje");
        double lecturaLuz = (double) leerNumeroPositivo("Lectura Inicial de Luz");
        String zona = leerTexto("Letra de la Zona");

        GarageDTO dto = new GarageDTO();
        dto.setNumeroGarage(numero);
        dto.setLecturaLuz(lecturaLuz);
        dto.setZona(zona);

        try {
            garageController.registrarGarage(usuarioSesion, dto);
            System.out.println("Garaje registrado correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void modificarGarage(int numeroGarage) throws ErrorNegocio {
        GarageDTO g;
        try {
            g = garageController.buscarPorNumero(usuarioSesion, numeroGarage);
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Garaje no encontrado: " + e.getMessage());
            return;
        }

        int nuevoNumero = leerNumeroPositivo("Nuevo Número de Garaje (actual: " + g.getNumeroGarage() + ")");
        g.setNumeroGarage(nuevoNumero);

        try {
            garageController.actualizarGarage(usuarioSesion, g);
            System.out.println("Garaje modificado correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarGarage() {
        int numeroGarage = leerNumeroPositivo("Número del Garaje a eliminar");
        try {
            garageController.eliminarGarage(usuarioSesion, numeroGarage);
            System.out.println("Garaje eliminado correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void altaZona() throws ErrorNegocio {
        String letra = leerTexto("Letra/Identificador de la Zona");
        String tipoVehiculo = leerTexto("Tipo de Vehículo permitido");
        int capacidad = leerNumeroPositivo("Capacidad máxima de vehículos");
        double ancho = (double) leerNumeroPositivo("Ancho de la zona");
        double largo = (double) leerNumeroPositivo("Largo de la zona");

        ZonaDTO dto = new ZonaDTO();
        dto.setLetra(letra);
        dto.setTipoVehiculo(tipoVehiculo);
        dto.setCapacidadVehiculos(capacidad);
        dto.setAncho(ancho);
        dto.setLargo(largo);

        zonaController.registrarZona(usuarioSesion, dto);
        System.out.println("Zona registrada con éxito.");
    }

    private void modificarZona(String letra) throws ErrorNegocio {
        ZonaDTO z;
        try {
            z = zonaController.buscarPorLetra(usuarioSesion, letra);
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Zona no encontrada: " + e.getMessage());
            return;
        }

        z.setCapacidadVehiculos(leerNumeroPositivo("Nueva Capacidad (actual: " + z.getCapacidadVehiculos() + ")"));
        zonaController.actualizarZona(usuarioSesion, z);
        System.out.println("Zona actualizada exitosamente.");
    }

    private void eliminarZona() throws ErrorNegocio {
        String letra = leerTexto("Letra de la Zona a eliminar");
        zonaController.eliminarZona(usuarioSesion, letra);
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