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

import model.Rol;
import model.Usuario;

import view.MenuAdmin;

public class MenuAdminImpl extends VistaImpl implements MenuAdmin {

    private static final int LIMITE_DNI = 8;
    private static final int LIMITE_TELEFONO = 10;
    private static final int LIMITE_CODIGO = 3;

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
                    mostrarSubmenuCRUD();
                    break;
                case 2:
                    ejecutarVentaGarage();
                    break;
                case 3:
                    ejecutarAsignacionVehiculo();
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

    private void mostrarSubmenuCRUD() {
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
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    // ---------------- OPERACIONES DE ENTIDADES (CRUD) ----------------

    private void altaUsuario(String tipo) {
        System.out.println("\n--- Alta de " + tipo.toUpperCase() + " ---");
        if ("socio".equalsIgnoreCase(tipo)) {
            String dni = leerTextoConLimite("DNI", LIMITE_DNI);
            String nombre = leerTexto("Nombre");
            String apellido = leerTexto("Apellido");
            String direccion = leerTexto("Dirección");
            String telefono = leerTextoConLimite("Teléfono", LIMITE_TELEFONO);
            String usuario = leerTexto("Nombre de Usuario");
            String clave = leerTexto("Clave");

            try {
                SocioDTO dto = new SocioDTO(
                        0, nombre, apellido, direccion, telefono, usuario, clave, Rol.SOCIO, dni, LocalDate.now()
                );
                socioController.registrarSocio(usuarioSesion, dto);
                System.out.println("Socio registrado con éxito.");
            } catch (ErrorNegocio ex) {
                System.out.println("Error al registrar socio: " + ex.getMessage());
            }

        } else if ("empleado".equalsIgnoreCase(tipo)) {
            String nombre = leerTexto("Nombre");
            String apellido = leerTexto("Apellido");
            String direccion = leerTexto("Dirección");
            String telefono = leerTextoConLimite("Teléfono", LIMITE_TELEFONO);
            String nombreUsuario = leerTexto("Nombre de Usuario");
            String clave = leerTexto("Clave");
            String codigo = leerTextoConLimite("Código de Empleado", LIMITE_CODIGO);
            String especialidad = leerTexto("Especialidad");

            try {
                EmpleadoDTO empDto = new EmpleadoDTO(
                        0, nombre, apellido, direccion, telefono, nombreUsuario, clave, Rol.EMPLEADO, codigo, especialidad
                );
                empleadoController.registrarEmpleado(usuarioSesion, empDto);
                System.out.println("Empleado registrado con éxito.");
            } catch (ErrorNegocio ex) {
                System.out.println("Error de negocio al registrar empleado: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error al registrar empleado: " + ex.getMessage());
            }
        }
    }

    private void modificarUsuario(int id, String tipo) {
        if ("socio".equalsIgnoreCase(tipo)) {
            try {
                SocioDTO socio = socioController.buscarSocioPorId(usuarioSesion, id);
                if (socio == null) {
                    System.out.println("Error: No existe el socio especificado.");
                    return;
                }

                String nuevoNombre = leerTexto("Nuevo Nombre (actual: " + socio.getNombre() + ")");
                String nuevoApellido = leerTexto("Nuevo Apellido (actual: " + socio.getApellido() + ")");
                String nuevaDireccion = leerTexto("Nueva Dirección (actual: " + socio.getDireccion() + ")");
                String nuevoTelefono = leerTextoConLimite("Nuevo Teléfono (actual: " + socio.getTelefono() + ")", LIMITE_TELEFONO);
                String nuevoDni = leerTextoConLimite("Nuevo DNI (actual: " + socio.getDni() + ")", LIMITE_DNI);

                SocioDTO socioActualizado = new SocioDTO(
                        id, nuevoNombre, nuevoApellido, nuevaDireccion, nuevoTelefono,
                        socio.getNombreUsuario(), socio.getClave(), Rol.SOCIO, nuevoDni, socio.getFechaIngreso()
                );

                socioController.modificarSocio(usuarioSesion, socioActualizado);
                System.out.println("Socio modificado exitosamente.");

            } catch (ErrorNegocio ex) {
                System.out.println("Error al modificar socio: " + ex.getMessage());
            }
        } else {
            System.out.println("Operación no admitida para este tipo de usuario.");
        }
    }

    private void eliminarUsuario(String tipo) {
        int id = leerNumeroPositivo("Ingrese ID a eliminar");
        if ("socio".equalsIgnoreCase(tipo)) {
            try {
                socioController.eliminarSocio(usuarioSesion, id);
                System.out.println("Socio eliminado con éxito.");
            } catch (ErrorNegocio ex) {
                System.out.println("Error al eliminar socio: " + ex.getMessage());
            }
        } else {
            System.out.println("Operación no soportada para empleados.");
        }
    }

    private void altaVehiculo() {
        System.out.println("\n--- Alta de Vehículo ---");
        String matricula = leerTexto("Matrícula");
        String nombre = leerTexto("Nombre / Marca");
        String tipo = leerTexto("Tipo / Modelo");
        float profundidad = (float) leerNumeroPositivo("Profundidad del vehículo");
        float ancho = (float) leerNumeroPositivo("Ancho del vehículo");

        try {
            VehiculoDTO dto = new VehiculoDTO(0, 0, 0, nombre, matricula, tipo, profundidad, ancho);
            vehiculoController.registrarVehiculo(usuarioSesion, dto);
            System.out.println("Vehículo registrado correctamente.");
        } catch (ErrorNegocio ex) {
            System.out.println("Error al registrar vehículo: " + ex.getMessage());
        }
    }

    private void modificarVehiculo(int idVehiculo) {
        try {
            VehiculoDTO v = vehiculoController.buscarVehiculoPorId(usuarioSesion, idVehiculo);
            if (v == null) {
                System.out.println("Error: Vehículo no encontrado.");
                return;
            }

            String nuevaMatricula = leerTexto("Nueva Matrícula (actual: " + v.getMatricula() + ")");
            String nuevoNombre = leerTexto("Nuevo Nombre/Marca (actual: " + v.getNombre() + ")");
            String nuevoTipo = leerTexto("Nuevo Tipo/Modelo (actual: " + v.getTipo() + ")");

            VehiculoDTO vActualizado = new VehiculoDTO(
                    v.getId(), v.getSocioId(), v.getEmpleadoId(), nuevoNombre, nuevaMatricula, nuevoTipo, v.getProfundidad(), v.getAncho()
            );
            vehiculoController.modificarVehiculo(usuarioSesion, vActualizado);
            System.out.println("Vehículo modificado con éxito.");

        } catch (ErrorNegocio ex) {
            System.out.println("Error al modificar vehículo: " + ex.getMessage());
        }
    }

    private void eliminarVehiculo() {
        System.out.println("\n--- Lista de Vehículos ---");
        try {
            vehiculoController.listarTodosLosVehiculos(usuarioSesion);

            int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a eliminar");
            VehiculoDTO vehiculo = vehiculoController.buscarVehiculoPorId(usuarioSesion, idVehiculo);

            if (vehiculo == null) {
                System.out.println("Error: No se encontró ningún vehículo con ese ID.");
                return;
            }

            vehiculoController.eliminarVehiculo(usuarioSesion, idVehiculo);
            System.out.println("Vehículo eliminado correctamente.");

        } catch (ErrorNegocio ex) {
            System.out.println("Error al eliminar vehículo: " + ex.getMessage());
        }
    }

    private void altaGaraje() {
        System.out.println("\n--- Alta de Garaje ---");
        int numero = leerNumeroPositivo("Número de Garaje");
        float lecturaLuz = (float) leerNumeroPositivo("Lectura Inicial de Luz");
        String letraZona = leerTexto("Letra de la Zona");

        GarageDTO dto = new GarageDTO(0, numero, lecturaLuz, false, null, null, letraZona);

        try {
            garageController.registrarGarage(usuarioSesion, dto);
            System.out.println("Garaje registrado correctamente.");
        } catch (RegistroNoEncontradoException ex) {
            System.out.println("Error: Zona no encontrada (" + ex.getMessage() + ")");
        } catch (ErrorNegocio ex) {
            System.out.println("Error de negocio: " + ex.getMessage());
        }
    }

    private void modificarGarage(int numeroGarage) {
        System.out.println("\n--- Modificación de Garaje ---");
        try {
            GarageDTO g = garageController.buscarPorNumero(usuarioSesion, numeroGarage);

            int nuevoNumero = leerNumeroPositivo("Nuevo Número de Garaje (actual: " + g.getNumeroGarage() + ")");
            g.setNumeroGarage(nuevoNumero);

            garageController.actualizarGarage(usuarioSesion, g);
            System.out.println("Garaje modificado correctamente.");

        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: Garaje no encontrado. " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        }
    }

    private void eliminarGarage() {
        System.out.println("\n--- Baja de Garaje ---");
        int numeroGarage = leerNumeroPositivo("Número del Garaje a eliminar");
        try {
            garageController.eliminarGarage(usuarioSesion, numeroGarage);
            System.out.println("Garaje eliminado correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: Garaje no encontrado. " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        }
    }

    private void altaZona() {
        System.out.println("\n--- Alta de Zona ---");
        String letra = leerTexto("Letra/Identificador de la Zona");
        String tipoVehiculo = leerTexto("Tipo de Vehículo permitido");
        int capacidad = leerNumeroPositivo("Capacidad máxima de vehículos");
        float ancho = (float) leerNumeroPositivo("Ancho de la zona");
        float largo = (float) leerNumeroPositivo("Largo de la zona");

        try {
            ZonaDTO dto = new ZonaDTO(0, letra, tipoVehiculo, capacidad, ancho, largo);
            zonaController.registrarZona(usuarioSesion, dto);
            System.out.println("Zona registrada con éxito.");
        } catch (ErrorNegocio e) {
            System.out.println("Error al registrar zona: " + e.getMessage());
        }
    }

    private void modificarZona(String letra) {
        System.out.println("\n--- Modificación de Zona ---");
        try {
            ZonaDTO z = zonaController.buscarPorLetra(usuarioSesion, letra);

            int nuevaCapacidad = leerNumeroPositivo("Nueva Capacidad (actual: " + z.getCapacidadVehiculos() + ")");
            z.setCapacidadVehiculos(nuevaCapacidad);

            zonaController.actualizarZona(usuarioSesion, z);
            System.out.println("Zona actualizada exitosamente.");

        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: Zona no encontrada. " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        }
    }

    private void eliminarZona() {
        System.out.println("\n--- Baja de Zona ---");
        String letra = leerTexto("Letra de la Zona a eliminar");
        try {
            zonaController.eliminarZona(usuarioSesion, letra);
            System.out.println("Zona eliminada correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: Zona no encontrada. " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        }
    }

    // ---------------- OPERACIONES TRANSACCIONALES Y ASIGNACIONES ----------------

    private void ejecutarVentaGarage() {
        System.out.println("\n--- Operación: Registrar Propiedad de Garaje ---");

        int socioId = leerNumeroPositivo("ID del Socio comprador");
        try {
            SocioDTO socio = socioController.buscarSocioPorId(usuarioSesion, socioId);
            if (socio == null) {
                System.out.println("Error: No existe un socio con el ID " + socioId);
                return;
            }

            int numeroGarage = leerNumeroPositivo("Número del Garaje");
            GarageDTO garage = garageController.buscarPorNumero(usuarioSesion, numeroGarage);

            if (garage.getSocioPropietario() != null) {
                System.out.println("Error: El garaje " + numeroGarage + " ya posee un propietario asignado.");
                return;
            }

            LocalDate fechaCompra = leerFechaValida("Fecha de Compra (YYYY-MM-DD)", false);
            PropiedadGarageDTO prop = new PropiedadGarageDTO(socio, garage, fechaCompra);

            propiedadGarageController.registrarPropiedad(usuarioSesion, prop);
            System.out.println("Propiedad de garaje registrada con éxito.");

        } catch (RegistroNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (ErrorNegocio ex) {
            System.out.println("Error de negocio: " + ex.getMessage());
        }
    }

    private void ejecutarAsignacionVehiculo() {
        System.out.println("\n--- Operación: Ocupación / Asignación de Garaje ---");

        int vehiculoId = leerNumeroPositivo("ID del Vehículo");
        try {
            VehiculoDTO vehiculo = vehiculoController.buscarVehiculoPorId(usuarioSesion, vehiculoId);
            if (vehiculo == null) {
                System.out.println("Error: No existe un vehículo con el ID " + vehiculoId);
                return;
            }

            int numeroGarage = leerNumeroPositivo("Número del Garaje a ocupar (0 para cancelar)");
            if (numeroGarage == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            GarageDTO garage = garageController.buscarPorNumero(usuarioSesion, numeroGarage);
            LocalDate fechaAsignacion = leerFechaValida("Fecha de Asignación (YYYY-MM-DD)", false);

            AsignacionVehiculoGarageDTO nuevaAsignacion = new AsignacionVehiculoGarageDTO(vehiculo, garage, fechaAsignacion);
            asignacionVehiculoGarageController.crearAsignacion(usuarioSesion, nuevaAsignacion);

            System.out.println("Vehículo asignado correctamente al garaje N° " + numeroGarage);

        } catch (RegistroNoEncontradoException ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (ErrorNegocio ex) {
            System.out.println("Error de negocio: " + ex.getMessage());
        }
    }

    private void ejecutarAsignacionEmpleado() {
        System.out.println("\n--- Operación: Carga de Personal en Zona ---");

        try {
            String idEmpleadoStr = String.valueOf(leerNumeroPositivo("ID del Empleado"));
            String idZonaStr = String.valueOf(leerNumeroPositivo("ID de la Zona"));
            String cantVehiculosStr = String.valueOf(leerNumeroPositivo("Cantidad de vehículos bajo su cargo"));

            String resultado = asignacionEmpleadoZonaController.crearAsignacion(usuarioSesion, idEmpleadoStr, idZonaStr, cantVehiculosStr);
            System.out.println(resultado);
        } catch (Exception ex) {
            System.out.println("Error al procesar la asignación del empleado: " + ex.getMessage());
        }
    }

    private void mostrarSubmenuConsultas() {
        System.out.println("\n--- REPORTES Y CONSULTAS ---");
        System.out.println("1. Ver Disponibilidad y Ocupación General de Garajes");
        System.out.println("2. Consultar Vehículos por Zona");
        System.out.println("3. Consultar Empleados por Zona");
        System.out.println("4. Volver");

        int op = leerEntero("Seleccione consulta");
        switch (op) {
            case 1:
                List<String> reporteDisponibilidad = garageController.consultarDisponibilidadGarages(usuarioSesion);
                if (reporteDisponibilidad != null && !reporteDisponibilidad.isEmpty()) {
                    System.out.println("\n--- Estado de Garajes ---");
                    reporteDisponibilidad.forEach(System.out::println);
                } else {
                    System.out.println("No se obtuvieron datos de disponibilidad.");
                }
                break;
            case 2:
                int idZ = leerNumeroPositivo("Ingrese ID de la Zona");
                vehiculoController.listarVehiculosPorZona(usuarioSesion, idZ);
                break;
            case 3:
                int idEmpleado = leerNumeroPositivo("Ingrese ID del Empleado");
                List<ZonaDTO> zonasEmpleado = empleadoController.listarZonasAsignadas(usuarioSesion, idEmpleado);
                System.out.println("Zonas asignadas al empleado: " + zonasEmpleado.size());
                for (ZonaDTO z : zonasEmpleado) {
                    System.out.println("- Zona: " + z.getLetra() + " (" + z.getTipoVehiculo() + ")");
                }
                break;
            case 4:
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida.");
        }
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