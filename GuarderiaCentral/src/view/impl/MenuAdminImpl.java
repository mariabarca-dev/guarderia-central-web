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
    // Se utiliza el Scanner heredado de VistaImpl

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
            System.out.println("6. Listados (Socios, Empleados, Vehículos, Garajes, Zonas)");
            System.out.println("7. Cerrar Sesión");

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
                    mostrarSubmenuListados();
                    break;
                case 7:
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
                listarSocios();
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
                listarVehiculos();
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
                listarGarajes();
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
                listarZonas();
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

    // ---------------- LISTADOS ----------------

    private void mostrarSubmenuListados() {
        System.out.println("\n--- LISTADOS ---");
        System.out.println("1. Listar Socios");
        System.out.println("2. Listar Empleados");
        System.out.println("3. Listar Vehículos");
        System.out.println("4. Listar Garajes");
        System.out.println("5. Listar Zonas");
        System.out.println("6. Volver");

        int op = leerEntero("Seleccione qué desea listar");
        switch (op) {
            case 1:
                listarSocios();
                break;
            case 2:
                listarEmpleados();
                break;
            case 3:
                listarVehiculos();
                break;
            case 4:
                listarGarajes();
                break;
            case 5:
                listarZonas();
                break;
            case 6:
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void listarSocios() {
        System.out.println("\n--- Socios registrados ---");
        try {
            List<SocioDTO> socios = socioController.listarTodosLosSocios(usuarioSesion);
            if (socios == null || socios.isEmpty()) {
                System.out.println("No hay socios registrados.");
                return;
            }
            for (SocioDTO s : socios) {
                System.out.println("ID: " + s.getId()
                        + " | " + s.getApellido() + ", " + s.getNombre()
                        + " | DNI: " + s.getDni()
                        + " | Tel: " + s.getTelefono()
                        + " | Ingreso: " + s.getFechaIngreso());
            }
        } catch (Exception ex) {
            System.out.println("Error al listar socios: " + ex.getMessage());
        }
    }

    private void listarEmpleados() {
        System.out.println("\n--- Empleados registrados ---");
        try {
            List<EmpleadoDTO> empleados = empleadoController.listarTodosLosEmpleados(usuarioSesion);
            if (empleados == null || empleados.isEmpty()) {
                System.out.println("No hay empleados registrados.");
                return;
            }
            for (EmpleadoDTO e : empleados) {
                System.out.println("ID: " + e.getId()
                        + " | Código: " + e.getCodigo()
                        + " | " + e.getApellido() + ", " + e.getNombre()
                        + " | Especialidad: " + e.getEspecialidad());
            }
        } catch (Exception ex) {
            System.out.println("Error al listar empleados: " + ex.getMessage());
        }
    }

    private void listarVehiculos() {
        System.out.println("\n--- Vehículos registrados ---");
        try {
            List<VehiculoDTO> vehiculos = vehiculoController.listarTodosLosVehiculos(usuarioSesion);
            if (vehiculos == null || vehiculos.isEmpty()) {
                System.out.println("No hay vehículos registrados.");
                return;
            }
            for (VehiculoDTO v : vehiculos) {
                System.out.println("ID: " + v.getId()
                        + " | Matrícula: " + v.getMatricula()
                        + " | Nombre: " + v.getNombre()
                        + " | Tipo: " + v.getTipo()
                        + " | ID Socio: " + v.getSocioId());
            }
        } catch (Exception ex) {
            System.out.println("Error al listar vehículos: " + ex.getMessage());
        }
    }

    private void listarGarajes() {
        System.out.println("\n--- Garajes registrados ---");
        try {
            List<GarageDTO> garajes = garageController.listarGarajes(usuarioSesion);
            if (garajes == null || garajes.isEmpty()) {
                System.out.println("No hay garajes registrados.");
                return;
            }
            for (GarageDTO g : garajes) {
                String propietario = (g.getSocioPropietario() != null) ? "DNI " + g.getSocioPropietario() : "Libre";
                String fecha = (g.getFechaCompra() != null) ? g.getFechaCompra().toString() : "-";
                System.out.println("N° " + g.getNumeroGarage()
                        + " | Zona: " + g.getZona()
                        + " | Luz: " + g.getLecturaLuz()
                        + " | Mantenimiento: " + (g.isServicioMantenimiento() ? "Sí" : "No")
                        + " | Propietario: " + propietario
                        + " | Fecha compra: " + fecha);
            }
        } catch (Exception ex) {
            System.out.println("Error al listar garajes: " + ex.getMessage());
        }
    }

    private void listarZonas() {
        System.out.println("\n--- Zonas registradas ---");
        try {
            List<ZonaDTO> zonas = zonaController.listarZonas(usuarioSesion);
            if (zonas == null || zonas.isEmpty()) {
                System.out.println("No hay zonas registradas.");
                return;
            }
            for (ZonaDTO z : zonas) {
                System.out.println("ID: " + z.getId()
                        + " | Letra: " + z.getLetra()
                        + " | Tipo: " + z.getTipoVehiculo()
                        + " | Capacidad: " + z.getCapacidadVehiculos()
                        + " | Ancho: " + z.getAncho()
                        + " | Largo: " + z.getLargo());
            }
        } catch (Exception ex) {
            System.out.println("Error al listar zonas: " + ex.getMessage());
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
        if ("socio".equalsIgnoreCase(tipo)) {
            listarSocios();
            int id = leerNumeroPositivo("Ingrese ID a eliminar");
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
        listarSocios();
        int socioId = leerNumeroPositivo("ID del Socio propietario");
        String matricula = leerTexto("Matrícula");
        String nombre = leerTexto("Nombre / Marca");
        String tipo = leerTexto("Tipo / Modelo (ej. MOTORHOME, CASA_RODANTE, CARAVANA, TRAILER)");
        float profundidad = (float) leerNumeroPositivo("Profundidad del vehículo");
        float ancho = (float) leerNumeroPositivo("Ancho del vehículo");

        try {
            VehiculoDTO dto = new VehiculoDTO(0, socioId, 0, nombre, matricula, tipo, profundidad, ancho);
            vehiculoController.registrarVehiculo(usuarioSesion, dto);
            System.out.println("Vehículo registrado correctamente.");
        } catch (ErrorNegocio ex) {
            System.out.println("Error al registrar vehículo: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error inesperado al registrar vehículo: " + ex.getMessage());
        }
    }

    private void modificarVehiculo(int idVehiculo) {
        try {
            VehiculoDTO v = vehiculoController.buscarVehiculoPorId(usuarioSesion, idVehiculo);
            if (v == null) {
                System.out.println("Error: Vehículo no encontrado.");
                return;
            }

            int nuevoSocioId = leerNumeroPositivo("Nuevo ID del Socio propietario (actual: " + v.getSocioId() + ")");
            String nuevaMatricula = leerTexto("Nueva Matrícula (actual: " + v.getMatricula() + ")");
            String nuevoNombre = leerTexto("Nuevo Nombre/Marca (actual: " + v.getNombre() + ")");
            String nuevoTipo = leerTexto("Nuevo Tipo/Modelo (actual: " + v.getTipo() + ")");
            float nuevaProfundidad = (float) leerNumeroPositivo("Nueva Profundidad (actual: " + v.getProfundidad() + ")");
            float nuevoAncho = (float) leerNumeroPositivo("Nuevo Ancho (actual: " + v.getAncho() + ")");

            VehiculoDTO vActualizado = new VehiculoDTO(
                    v.getId(), nuevoSocioId, v.getEmpleadoId(), nuevoNombre, nuevaMatricula, nuevoTipo, nuevaProfundidad, nuevoAncho
            );
            vehiculoController.modificarVehiculo(usuarioSesion, vActualizado);
            System.out.println("Vehículo modificado con éxito.");

        } catch (ErrorNegocio ex) {
            System.out.println("Error al modificar vehículo: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error inesperado al modificar vehículo: " + ex.getMessage());
        }
    }

    private void eliminarVehiculo() {
        listarVehiculos();
        try {
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
        } catch (Exception ex) {
            System.out.println("Error inesperado al eliminar vehículo: " + ex.getMessage());
        }
    }

    private void altaGaraje() {
        System.out.println("\n--- Alta de Garaje ---");
        int numero = leerNumeroPositivo("Número de Garaje");
        float lecturaLuz = (float) leerNumeroPositivo("Lectura Inicial de Luz");
        listarZonas();
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
        listarGarajes();
        int numeroGarage = leerNumeroPositivo("Número del Garaje a eliminar");
        try {
            garageController.eliminarGarage(usuarioSesion, numeroGarage);
            System.out.println("Garaje eliminado correctamente.");
        } catch (RegistroNoEncontradoException e) {
            System.out.println("Error: Garaje no encontrado. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void altaZona() {
        System.out.println("\n--- Alta de Zona ---");
        String letra = leerTexto("Letra/Identificador de la Zona");
        String tipoVehiculo = leerTexto("Tipo de Vehículo permitido (ej. MOTORHOME, CASA_RODANTE, CARAVANA, TRAILER)");
        int capacidad = leerNumeroPositivo("Capacidad máxima de vehículos");
        float ancho = (float) leerNumeroPositivo("Ancho de la zona");
        float largo = (float) leerNumeroPositivo("Largo de la zona");

        try {
            ZonaDTO dto = new ZonaDTO(0, letra, tipoVehiculo, capacidad, ancho, largo);
            zonaController.registrarZona(usuarioSesion, dto);
            System.out.println("Zona registrada con éxito.");
        } catch (ErrorNegocio e) {
            System.out.println("Error al registrar zona: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al registrar zona: " + e.getMessage());
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
        listarZonas();
        String letra = leerTexto("Letra de la Zona a eliminar");
        try {
            zonaController.eliminarZona(usuarioSesion, letra);
            System.out.println("Zona eliminada correctamente.");
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------------- OPERACIONES TRANSACCIONALES Y ASIGNACIONES ----------------

    private void ejecutarVentaGarage() {
        System.out.println("\n--- Operación: Registrar Propiedad de Garaje ---");

        listarSocios();
        int socioId = leerNumeroPositivo("ID del Socio comprador");
        try {
            SocioDTO socio = socioController.buscarSocioPorId(usuarioSesion, socioId);
            if (socio == null) {
                System.out.println("Error: No existe un socio con el ID " + socioId);
                return;
            }

            listarGarajes();
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

        listarVehiculos();
        int vehiculoId = leerNumeroPositivo("ID del Vehículo");
        try {
            VehiculoDTO vehiculo = vehiculoController.buscarVehiculoPorId(usuarioSesion, vehiculoId);
            if (vehiculo == null) {
                System.out.println("Error: No existe un vehículo con el ID " + vehiculoId);
                return;
            }

            listarGarajes();
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
            listarEmpleados();
            String idEmpleadoStr = String.valueOf(leerNumeroPositivo("ID del Empleado"));
            listarZonas();
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
        try {
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
                    listarZonas();
                    int idZ = leerNumeroPositivo("Ingrese ID de la Zona");
                    List<VehiculoDTO> vehiculosZona = vehiculoController.listarVehiculosPorZona(usuarioSesion, idZ);
                    System.out.println("\n--- Vehículos en la Zona " + idZ + " ---");
                    if (vehiculosZona == null || vehiculosZona.isEmpty()) {
                        System.out.println("No se encontraron vehículos en esta zona.");
                    } else {
                        for (VehiculoDTO v : vehiculosZona) {
                            System.out.println("- Matrícula: " + v.getMatricula() +
                                    " | Nombre/Marca: " + v.getNombre() +
                                    " | Tipo: " + v.getTipo());
                        }
                    }
                    break;
                case 3:
                    listarEmpleados();
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
        } catch (Exception ex) {
            System.out.println("Error al procesar la consulta: " + ex.getMessage());
        }
    }

    // ---------------- MÉTODOS REUTILIZABLES DE LECTURA ----------------

    @Override
    protected String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim(); // Utiliza el scanner heredado de VistaImpl
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
