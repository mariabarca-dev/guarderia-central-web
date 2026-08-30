package view.impl;

import controller.AdminController;
import dto.*;

// 🔹 Importaciones de excepciones necesarias [cite: 2]
import exceptions.CodigoEmpleadoDuplicadoException;
import exceptions.DniDuplicadoException;
import exceptions.ErrorNegocio;
import exceptions.GarageYaOcupadoException;
import exceptions.GarageYaVendidoException;
import exceptions.MatriculaDuplicadaException;
import exceptions.RegistroNoEncontradoException;
import exceptions.ZonaSinCapacidadException;
// 🔹 Importaciones de Mappers necesarios para la conversión 
import mapper.EmpleadoMapper;
import mapper.GarageMapper;
import mapper.SocioMapper;
import mapper.VehiculoMapper;
import mapper.ZonaMapper;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
// 🔹 Modelos usados solo LOCALMENTE en la vista para interacción 
import model.Administrador;
import model.Empleado;
import model.Rol;
import model.TipoVehiculo;
import service.ZonaService;

/**
 * Menú de administración central. Esta clase implementa la vista y gestiona la
 * interacción con el usuario, comunicándose con la capa de controlador
 * exclusivamente a través de DTOs (Data Transfer Objects).
 */
public class MenuAdminImpl extends VistaImpl {

    // Constantes globales de límites (se mantienen igual)
    private static final int LIMITE_DNI = 8;
    private static final int LIMITE_USUARIO = 20;
    private static final int LIMITE_TELEFONO = 10;
    private static final int LIMITE_CODIGO = 3;
    private static final float LIMITE_AMH = 10.00f;
    private static final float LIMITE_LMH = 20.00f;
    private static final float LIMITE_ACR = 15.00f;
    private static final float LIMITE_LCR = 25.00f;
    private static final float LIMITE_ACA = 10.00f;
    private static final float LIMITE_LCA = 25.00f;
    private static final float LIMITE_ATR = 10.00f;
    private static final float LIMITE_LTR = 40.00f;
    private static final int LIMITE_CAPACIDAD1 = 10;
    private static final int LIMITE_CAPACIDAD2 = 15;

    private final AdminController adminController;
    private final Scanner scanner = new Scanner(System.in);

    public MenuAdminImpl(AdminController adminController) {
        this.adminController = adminController;
    }

    @Override
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            
            System.out.println("Panel de Administración Central");
            System.out.println("1. ABM Entidades (Socios, Empleados, Vehículos, Garajes, Zonas)");
            System.out.println("2. Registrar Venta de Garaje a Socio");
            System.out.println("3. Asignar Vehículo a Garaje");
            System.out.println("4. Asignar Empleado a Zona");
            System.out.println("5. Consultas de Ocupación y Disponibilidad");
            System.out.println("6. Cerrar Sesión");

            int opcion = leerEntero("Seleccione una opción");
            switch (opcion) {
                case 1: {
                    
                    try {
                        mostrarSubmenuCRUD();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                case 2:
                {
                    
                    try {
                        ejecutarVentaGarage();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;

                case 3:
                {
                    
                    try {
                        ejecutarAsignacionVehiculo();
                    } catch (ErrorNegocio ex) {
                        Logger.getLogger(MenuAdminImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
            case 1: // Alta Socio
                altaUsuario("socio");
                break;
            case 2: // Modificar Socio
                //listarTodosLosSocios(); // 🔹 Nuevo método auxiliar para listar
                int idSocio = leerNumeroPositivo("Ingrese el ID del socio a modificar");
                modificarUsuario(idSocio);
                break;
            case 3: // Baja Socio
                eliminarUsuario(); // 🔹 Nuevo método específico
                break;

            case 4: // Alta Empleado
                altaUsuario("empleado");
                break;
            case 5: // Modificar Empleado
                // listarTodosLosEmpleados(); // 🔹 Nuevo método auxiliar
                int idEmpleado = leerNumeroPositivo("Ingrese el ID del empleado a modificar");
                modificarUsuario(idEmpleado);
                break;
            case 6: // Baja Empleado
                //eliminarEmpleado(); // 🔹 Nuevo método específico
                break;

            case 7: // Alta Vehículo
                altaVehiculo();
                break;
            case 8: // Modificar Vehículo
                // listarTodosLosVehiculos(); // 🔹 Nuevo método auxiliar
                int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a modificar");
                modificarVehiculo(idVehiculo);
                break;
            case 9: // Baja Vehículo
                eliminarVehiculo(); // 🔹 Nuevo método específico
                break;

            case 10: // Alta Garaje
                altaGaraje();
                break;
            case 11: // Modificar Garaje
                //listarTodosLosGarages(); // 🔹 Nuevo método auxiliar
                int idGaraje = leerNumeroPositivo("Ingrese el ID del garaje a modificar");
                modificarGarage(idGaraje);
                break;
            case 12: // Baja Garaje
                eliminarGarage(); // 🔹 Nuevo método específico
                break;

            case 13: // Alta Zona
                altaZona();
                break;
            case 14: // Modificar Zona
                // listarTodasLasZonas(); // 🔹 Nuevo método auxiliar
                int idZona = leerNumeroPositivo("Ingrese el ID de la zona a modificar");
                modificarZona(idZona);
                break;
            case 15: // Baja Zona
                eliminarZona(); // 🔹 Nuevo método específico
                break;

            case 16: // Volver
                System.out.println("Volviendo al menú principal...");
                break;

            default:
                System.out.println("Opción inválida.");
        }

    }

    private void ejecutarVentaGarage() throws ErrorNegocio {
        System.out.println("Operación: Propiedad de Garaje");

        int socioId = leerNumeroPositivo("ID del Socio comprador");
        SocioDTO socio = adminController.buscarSocioPorId(socioId); // Verifica que este método exista en el controller
        if (socio == null) {
            System.out.println("Error: no existe un socio con ese ID.");
            return;
        }

        GarageDTO garage = null;
        boolean garageValido = false;

        do {
            int garageId = leerNumeroPositivo("ID del Garaje");
            garage = adminController.buscarGaragePorId(garageId);

            if (garage == null) {
                System.out.println("Error: no existe un garaje con ese ID.");
            } else {
                try {
                    // 🔹 Validación: Si el ID del socio es distinto de 0 o nulo, ya tiene dueño
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

            // 🔹 Crear el DTO
            PropiedadGarageDTO prop = new PropiedadGarageDTO(socio, garage, fechaCompra);

            // 🔹 Llamar al controlador
            adminController.asignarPropiedadGarage(prop);

            System.out.println("Propiedad de garaje registrada correctamente.");

        } catch (ErrorNegocio e) {
            // Aquí muestras el mensaje real: "Error: La fecha de compra no puede ser anterior..."
            System.out.println(e.getMessage());
        }

    }

    private void ejecutarAsignacionVehiculo() throws ErrorNegocio {
        System.out.println("Operación: Ocupación de Garaje");

        int vehiculoId = leerNumeroPositivo("ID del Vehículo");
        VehiculoDTO vehiculo = adminController.buscarVehiculoPorId(vehiculoId);
        if (vehiculo == null) {
            System.out.println("Error: no existe un vehículo con ese ID.");
            return;
        }

        GarageDTO garage = null;
        boolean garageValido = false;

        do {
            int garajeId = leerNumeroPositivo("ID del Garaje a ocupar");
            garage = adminController.buscarGaragePorId(garajeId);

            if (garage == null) {
                System.out.println("Error: no existe un garaje con ese ID.");
            } else {
                try {
                    // 🔹 Verificamos si existe una asignación activa (el controller debe buscar por ID de garage)
                    AsignacionVehiculoGarageDTO asignacion = adminController.buscarAsignacionPorGarage(garajeId);
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

            // 🔹 Crear el DTO
            AsignacionVehiculoGarageDTO nuevaAsignacion = new AsignacionVehiculoGarageDTO(vehiculo, garage, fechaAsignacion);

            // 🔹 Llamar al controlador
            adminController.asignarVehiculoAGarageDTO(nuevaAsignacion);

            System.out.println("Vehículo asignado correctamente al garaje.");

    }
    

    private void ejecutarAsignacionEmpleado() throws ErrorNegocio {
        System.out.println("Operación: Carga de Personal en Zona");

        int empleadoId = leerNumeroPositivo("ID del Empleado");
        EmpleadoDTO empleado = adminController.buscarEmpleadoPorId(empleadoId);
        if (empleado == null) {
            System.out.println("Error: no existe un empleado con ese ID.");
            return;
        }

        int zonaId = leerNumeroPositivo("ID de la Zona");
        ZonaDTO zona = adminController.buscarZonaPorId(zonaId);
        if (zona == null) {
            System.out.println("Error: no existe una zona con ese ID.");
            return;
        }

        int vehiculosACargo = leerNumeroPositivo("Cantidad de vehículos bajo su cargo");

        // 🔹 Validación: usamos el DTO de zona para obtener la capacidad o consultar al controller
        List<VehiculoDTO> vehiculosEnZona = adminController.listarVehiculosPorZona(zonaId);
        int cantidadReal = (vehiculosEnZona != null) ? vehiculosEnZona.size() : 0;

        if (cantidadReal < vehiculosACargo) {
            System.out.println("Error: la zona solo tiene " + cantidadReal + " vehículos.");
            return;
        }

        System.out.println("Vehículos disponibles en la zona:");
        for (VehiculoDTO v : vehiculosEnZona) {
            System.out.println("ID: " + v.getId() + " - Matrícula: " + v.getMatricula());
        }

        // 🔹 Selección de IDs de vehículos a cargo
        List<Integer> idsSeleccionados = new ArrayList<>();
        for (int i = 0; i < vehiculosACargo; i++) {
            int idVehiculo = leerNumeroPositivo("Ingrese ID del vehículo #" + (i + 1) + " a cargo");
            idsSeleccionados.add(idVehiculo);
        }

        // 🔹 Creamos el DTO de asignación enviando los objetos DTO correspondientes
        AsignacionEmpleadoZonaDTO asignacion = new AsignacionEmpleadoZonaDTO(empleado, zona, vehiculosACargo);

        // Si tu lógica requiere los IDs de los vehículos, asegúrate de que el DTO 
        // también los contenga o agrégalos mediante un setter:
        // asignacion.setIdsVehiculos(idsSeleccionados);
        adminController.asignarEmpleadoAZona(asignacion);
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
                // 🔹 Llamada a un método que devuelva una lista de DTOs o los imprima
                adminController.listarDisponibilidadGarages();
                break;
            case 2:
                int idZ = leerNumeroPositivo("Ingrese ID de la Zona");
                adminController.listarVehiculosPorZona(idZ);
                break;
            case 3:
                int idZonaEmp = leerNumeroPositivo("Ingrese ID de la Zona");
                adminController.listarEmpleadosPorZona(idZonaEmp);
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ---------------- MÉTODOS REUTILIZABLES ----------------
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
        int numero = -1;
        while (numero < 0) {
            try {
                System.out.print(mensaje + ": ");
                numero = Integer.parseInt(scanner.nextLine().trim());
                if (numero < 0) {
                    System.out.println("Error: ingrese un número mayor o igual a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar solo números.");
            }
        }
        return numero;
    }

    private double leerNumeroPositivoDouble(String mensaje) {
        double numero = -1.0;
        while (numero < 0) {
            try {
                System.out.print(mensaje + ": ");
                numero = Double.parseDouble(scanner.nextLine().trim());
                if (numero < 0) {
                    System.out.println("Error: el valor debe ser positivo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número válido.");
            }
        }
        return numero;
    }

    private LocalDate leerFechaValida(String mensaje, boolean noFutura) {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print(mensaje + " (YYYY-MM-DD): ");
                fecha = LocalDate.parse(scanner.nextLine().trim());
                if (noFutura && fecha.isAfter(LocalDate.now())) {
                    System.out.println("Error: la fecha no puede ser futura.");
                } else {
                    valido = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: formato de fecha incorrecto. Asegúrese de usar YYYY-MM-DD.");
            }
        }
        return fecha;
    }

    private char leerLetraZona(String mensaje) {
        String entrada;
        do {
            System.out.print(mensaje + ": ");
            entrada = scanner.nextLine().trim();
            if (entrada.length() != 1 || !Character.isLetter(entrada.charAt(0))) {
                System.out.println("Error: debe ser una sola letra válida.");
                entrada = "";
            }
        } while (entrada.isEmpty());
        return entrada.charAt(0);
    }

    private void altaUsuario(String tipo) throws ErrorNegocio {
        // Atributos comunes
        String nombre = leerTextoConLimite("Ingrese nombre", LIMITE_USUARIO);
        String nombreUsuario = leerTextoConLimite("Ingrese nombre de usuario", LIMITE_USUARIO);
        String clave = leerTextoConLimite("Ingrese clave", LIMITE_USUARIO);
        String direccion = leerTextoConLimite("Ingrese dirección", LIMITE_USUARIO);

        String telefono;
        do {
            telefono = leerTextoConLimite("Ingrese teléfono", LIMITE_TELEFONO);
            if (!telefono.matches("\\d+")) {
                System.out.println("Error: solo números.");
                telefono = "";
            }
        } while (telefono.isEmpty());

        // Atributos específicos según tipo
        switch (tipo.toLowerCase()) {
            case "socio":
                String dni = leerTextoConLimite("Ingrese DNI", LIMITE_DNI);
                if (!dni.matches("\\d+")) {
                    System.out.println("Error: DNI numérico.");
                    return; // corta el flujo si no es válido
                }

                LocalDate fechaIngreso = leerFechaValida("Fecha de ingreso (YYYY-MM-DD)", true);

                SocioDTO socio = new SocioDTO(
                        0, nombre, direccion, telefono, nombreUsuario, clave, Rol.SOCIO, dni, fechaIngreso
                );

                try {
                    adminController.registrarUsuario(socio);
                    System.out.println("Socio registrado correctamente.");
                } catch (DniDuplicadoException e) {
                    System.out.println(e.getMessage());
                } catch (ErrorNegocio e) {
                    System.out.println("Error de negocio: " + e.getMessage());
                }
                break;

            case "empleado":
                String codigo = leerTextoConLimite("Ingrese código de empleado", LIMITE_CODIGO);
                if (!codigo.matches("\\d+")) {
                    System.out.println("Error: código numérico.");
                    return; // corta el flujo si no es válido
                }

                String especialidad = leerTextoConLimite("Ingrese especialidad", LIMITE_USUARIO);

                EmpleadoDTO empleado = new EmpleadoDTO(
                        0, nombre, direccion, telefono, nombreUsuario, clave, Rol.EMPLEADO, codigo, especialidad
                );

                try {
                    adminController.registrarUsuario(empleado);
                    System.out.println("Empleado registrado correctamente.");
                } catch (ErrorNegocio e) {
                    System.out.println("Error de negocio: " + e.getMessage());
                }
                break;

            case "administrador":
                // 🔹 Registro mediante AdministradorDTO
                adminController.registrarUsuario(new AdministradorDTO(0, nombre, direccion, telefono, nombreUsuario, clave, Rol.ADMINISTRADOR));
                break;

            default:
                System.out.println("Tipo de usuario inválido.");
        }
    }

    private void modificarUsuario(int id) {
        // Obtenemos la lista completa de usuarios
        List<UsuarioDTO> usuarios = adminController.listarTodosLosUsuarios();

        // Buscamos el usuario con el ID indicado
        UsuarioDTO usuario = null;
        for (UsuarioDTO u : usuarios) {
            if (u.getId() == id) {
                usuario = u;
                break;
            }
        }

        if (usuario == null) {
            System.out.println("No se encontró ningún usuario con ese ID.");
            return;
        }

        System.out.println("Modificando a: " + usuario.getNombre());
        System.out.println("a. Nombre / b. Usuario / c. Clave / d. Dirección / e. Teléfono");

        if (usuario instanceof SocioDTO) {
            System.out.println("f. Fecha ingreso / g. DNI");
        } else if (usuario instanceof EmpleadoDTO) {
            System.out.println("f. Código empleado / g. Especialidad");
        }

        char opcion = leerLetraZona("Opción");

        switch (opcion) {
            case 'a':
                usuario.setNombre(leerTextoConLimite("Nuevo nombre", LIMITE_USUARIO));
                break;
            case 'b':
                usuario.setNombreUsuario(leerTextoConLimite("Nuevo usuario", LIMITE_USUARIO));
                break;
            case 'c':
                usuario.setClave(leerTextoConLimite("Nueva clave", LIMITE_USUARIO));
                break;
            case 'd':
                usuario.setDireccion(leerTextoConLimite("Nueva dirección", LIMITE_USUARIO));
                break;
            case 'e':
                usuario.setTelefono(leerTextoConLimite("Nuevo teléfono", LIMITE_TELEFONO));
                break;
            case 'f':
                if (usuario instanceof SocioDTO) {
                    ((SocioDTO) usuario).setFechaIngreso(leerFechaValida("Nueva fecha (YYYY-MM-DD)", true));
                } else {
                    ((EmpleadoDTO) usuario).setCodigo(leerTextoConLimite("Nuevo código de empleado", LIMITE_CODIGO));
                }
                break;
            case 'g':
                if (usuario instanceof SocioDTO) {
                    ((SocioDTO) usuario).setDni(leerTextoConLimite("Nuevo DNI", LIMITE_DNI));
                } else {
                    ((EmpleadoDTO) usuario).setEspecialidad(leerTextoConLimite("Nueva especialidad", LIMITE_USUARIO));
                }
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        try {
            adminController.modificarUsuario(usuario);
            System.out.println("Usuario actualizado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        System.out.println("--- Lista de Usuarios ---");
        List<UsuarioDTO> usuarios = adminController.listarTodosLosUsuarios();

        // Mostramos los usuarios con sus IDs
        for (UsuarioDTO u : usuarios) {
            System.out.println("ID: " + u.getId() + " | Nombre: " + u.getNombre() + " | Usuario: " + u.getNombreUsuario());
        }

        int id = leerNumeroPositivo("Ingrese el ID del usuario a eliminar");

        // Verificamos si existe el usuario con ese ID
        UsuarioDTO usuarioAEliminar = null;
        for (UsuarioDTO u : usuarios) {
            if (u.getId() == id) {
                usuarioAEliminar = u;
                break;
            }
        }

        if (usuarioAEliminar == null) {
            System.out.println("No se encontró ningún usuario con ese ID.");
            return;
        }

        try {
            adminController.eliminarUsuario(id);
            System.out.println("Usuario eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    private void altaGaraje() {
        int numeroGarage = leerNumeroPositivo("Ingrese número de garage");
        double lecturaLuz = leerNumeroPositivoDouble("Ingrese lectura del contador de luz");

        char respMantenimiento = leerLetraZona("¿Tiene servicio de mantenimiento? (s/n)");
        boolean servicioMantenimiento = (respMantenimiento == 's' || respMantenimiento == 'S');

        // --- Selección de socio propietario ---
        System.out.println("--- Lista de Socios ---");
        List<SocioDTO> socios = adminController.listarTodosLosSocios();
        for (SocioDTO s : socios) {
            System.out.println("ID: " + s.getId() + " | Nombre: " + s.getNombre() + " | DNI: " + s.getDni());
        }

        int idSocio = leerNumeroPositivo("Ingrese ID del socio propietario");
        SocioDTO socioPropietario = null;
        for (SocioDTO s : socios) {
            if (s.getId() == idSocio) {
                socioPropietario = s;
                break;
            }
        }
        if (socioPropietario == null) {
            System.out.println("Error: no existe un socio con ese ID.");
            return;
        }

        // --- Fecha de compra (si fue vendido) ---
        LocalDate fechaCompra = null;
        char vendidoResp = leerLetraZona("¿El garaje fue vendido? (s/n)");
        if (vendidoResp == 's' || vendidoResp == 'S') {
            fechaCompra = leerFechaValida("Ingrese fecha de compra (YYYY-MM-DD)", true);
        }

        // --- Selección de zona ---
        System.out.println("--- Lista de Zonas ---");
        List<ZonaDTO> zonas = adminController.listarTodasLasZonas();
        for (ZonaDTO z : zonas) {
            System.out.println("Letra: " + z.getLetra() + " | Capacidad: " + z.getCapacidadVehiculos());
        }

        String letraZona = leerTextoConLimite("Ingrese letra de la zona", 1);
        ZonaDTO zonaSeleccionada = null;
        for (ZonaDTO z : zonas) {
            if (z.getLetra().equalsIgnoreCase(letraZona)) {
                zonaSeleccionada = z;
                break;
            }
        }
        if (zonaSeleccionada == null) {
            System.out.println("Error: no existe una zona con esa letra.");
            return;
        }
        if (zonaSeleccionada.getCapacidadVehiculos() <= 0) {
            System.out.println("Error: la zona no tiene capacidad disponible.");
            return;
        }

        // 🔹 Ajustamos capacidad en el DTO directamente
        zonaSeleccionada.setCapacidadVehiculos(zonaSeleccionada.getCapacidadVehiculos() - 1);

        // --- Registro del garaje ---
        GarageDTO nuevoGarage = new GarageDTO(
                0,
                numeroGarage,
                (float) lecturaLuz,
                servicioMantenimiento,
                socioPropietario.getDni(), // pasamos DNI como String
                fechaCompra,
                zonaSeleccionada.getLetra()
        );

        try {
            adminController.registrarGaraje(nuevoGarage);

            // 🔹 Ajustamos capacidad en el DTO y lo mandamos al controller
            zonaSeleccionada.setCapacidadVehiculos(zonaSeleccionada.getCapacidadVehiculos() - 1);
            adminController.modificarZona(zonaSeleccionada);

            System.out.println("Garaje registrado correctamente en la zona " + zonaSeleccionada.getLetra()
                    + " para el socio " + socioPropietario.getNombre());
        } catch (Exception e) {
            System.out.println("Error al registrar garaje: " + e.getMessage());
        }
    }

    private void modificarGarage(int idGarage) {
        // --- Buscar garage en la lista ---
        List<GarageDTO> garages = adminController.listarTodosLosGarages();
        GarageDTO garage = null;
        for (GarageDTO g : garages) {
            if (g.getId() == idGarage) {
                garage = g;
                break;
            }
        }

        if (garage == null) {
            System.out.println("No se encontró ningún garage con ese ID.");
            return;
        }

        System.out.println("Modificando Garaje ID: " + idGarage);
        System.out.println("a. Número / b. Luz / c. Mantenimiento / d. Socio / e. Fecha / f. Zona");

        char opcion = leerLetraZona("Opción");

        switch (opcion) {
            case 'a':
                garage.setNumeroGarage(leerNumeroPositivo("Nuevo número"));
                break;
            case 'b':
                garage.setLecturaLuz((float) leerNumeroPositivoDouble("Nueva lectura"));
                break;
            case 'c':
                char respMantenimiento = leerLetraZona("¿Tiene mantenimiento? (s/n)");
                garage.setServicioMantenimiento(respMantenimiento == 's' || respMantenimiento == 'S');
                break;
            case 'd':
                // Selección de socio propietario
                System.out.println("--- Lista de Socios ---");
                List<SocioDTO> socios = adminController.listarTodosLosSocios();
                for (SocioDTO s : socios) {
                    System.out.println("ID: " + s.getId() + " | Nombre: " + s.getNombre() + " | DNI: " + s.getDni());
                }

                int idSocio = leerNumeroPositivo("ID nuevo socio");
                SocioDTO socioPropietario = null;
                for (SocioDTO s : socios) {
                    if (s.getId() == idSocio) {
                        socioPropietario = s;
                        break;
                    }
                }

                if (socioPropietario != null) {
                    garage.setSocioPropietario(socioPropietario.getDni()); // guardamos DNI como String
                } else {
                    System.out.println("Error: Socio no encontrado.");
                }
                break;
            case 'e':
                char vendidoResp = leerLetraZona("¿Fue vendido? (s/n)");
                if (vendidoResp == 's' || vendidoResp == 'S') {
                    garage.setFechaCompra(leerFechaValida("Fecha (YYYY-MM-DD)", true));
                } else {
                    garage.setFechaCompra(null);
                }
                break;
            case 'f':
                // Selección de zona
                System.out.println("--- Lista de Zonas ---");
                List<ZonaDTO> zonas = adminController.listarTodasLasZonas();
                for (ZonaDTO z : zonas) {
                    System.out.println("Letra: " + z.getLetra() + " | Capacidad: " + z.getCapacidadVehiculos());
                }

                String letraZona = leerTextoConLimite("Ingrese letra de la zona", 1);
                ZonaDTO zonaSeleccionada = null;
                for (ZonaDTO z : zonas) {
                    if (z.getLetra().equalsIgnoreCase(letraZona)) {
                        zonaSeleccionada = z;
                        break;
                    }
                }

                if (zonaSeleccionada == null) {
                    System.out.println("Error: zona no encontrada.");
                    return;
                }
                if (zonaSeleccionada.getCapacidadVehiculos() <= 0) {
                    System.out.println("Error: la zona no tiene capacidad disponible.");
                    return;
                }

                garage.setZona(zonaSeleccionada.getLetra());
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        // 🔹 Persistimos el DTO actualizado
        adminController.modificarGarage(garage);
        System.out.println("Garaje actualizado correctamente.");
    }

    private void eliminarGarage() {
        System.out.println("--- Lista de Garajes ---");
        List<GarageDTO> garages = adminController.listarTodosLosGarages();
        for (GarageDTO g : garages) {
            System.out.println("ID: " + g.getId() + " | Número: " + g.getNumeroGarage()
                    + " | Socio: " + g.getSocioPropietario()
                    + " | Zona: " + g.getZona());
        }

        int idGarage = leerNumeroPositivo("Ingrese el ID del garaje a eliminar");

        // Buscar garage en la lista
        GarageDTO garageAEliminar = null;
        for (GarageDTO g : garages) {
            if (g.getId() == idGarage) {
                garageAEliminar = g;
                break;
            }
        }

        if (garageAEliminar == null) {
            System.out.println("No se encontró ningún garaje con ese ID.");
            return;
        }

        try {
            adminController.eliminarGarage(idGarage);
            System.out.println("Garaje eliminado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar garaje: " + e.getMessage());
        }
    }

    private void altaZona() {
        char letra = leerLetraZona("Ingrese letra identificadora de la zona");

        System.out.println("Seleccione tipo de vehículo permitido:");
        System.out.println("a. Motorhomes / b. Casas rodantes / c. Caravanas / d. Trailers");

        char opcionTipo = leerLetraZona("Opción");

        TipoVehiculo tipo;
        float ancho, largo;
        int capacidad;

        switch (opcionTipo) {
            case 'a':
                tipo = TipoVehiculo.MOTORHOME;
                ancho = LIMITE_AMH;
                largo = LIMITE_LMH;
                capacidad = LIMITE_CAPACIDAD2;
                break;
            case 'b':
                tipo = TipoVehiculo.CASA_RODANTE;
                ancho = LIMITE_ACR;
                largo = LIMITE_LCR;
                capacidad = LIMITE_CAPACIDAD1;
                break;
            case 'c':
                tipo = TipoVehiculo.CARAVANA;
                ancho = LIMITE_ACA;
                largo = LIMITE_LCA;
                capacidad = LIMITE_CAPACIDAD2;
                break;
            case 'd':
                tipo = TipoVehiculo.TRAILER;
                ancho = LIMITE_ATR;
                largo = LIMITE_LTR;
                capacidad = LIMITE_CAPACIDAD1;
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        // 🔹 Construcción del DTO con constructor directo
        ZonaDTO zonaDto = new ZonaDTO(
                0, // ID inicial
                Character.toString(letra), // convertimos char → String de forma clara
                tipo.name(),
                capacidad,
                ancho,
                largo
        );

        try {
            adminController.registrarZona(zonaDto);
            System.out.println("Zona registrada correctamente con letra " + letra);
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private void modificarZona(int idZona) {
        System.out.println("--- Lista de Zonas ---");
        List<ZonaDTO> zonas = adminController.listarTodasLasZonas();
        for (ZonaDTO z : zonas) {
            System.out.println("ID: " + z.getId() + " | Letra: " + z.getLetra()
                    + " | Tipo: " + z.getTipoVehiculo()
                    + " | Capacidad: " + z.getCapacidadVehiculos());
        }

        // Buscar zona en la lista
        ZonaDTO zona = null;
        for (ZonaDTO z : zonas) {
            if (z.getId() == idZona) {
                zona = z;
                break;
            }
        }

        if (zona == null) {
            System.out.println("No se encontró ninguna zona con ese ID.");
            return;
        }

        System.out.println("Modificando Zona: " + zona.getLetra());
        System.out.println("a. Letra / b. Tipo de vehículo");
        char opcion = leerLetraZona("Opción");

        switch (opcion) {
            case 'a':
                char nuevaLetra = leerLetraZona("Ingrese nueva letra");
                zona.setLetra(Character.toString(nuevaLetra));
                break;

            case 'b':
                System.out.println("Seleccione nuevo tipo (a. Motorhome, b. Casa Rodante, c. Caravana, d. Trailer)");
                char op = leerLetraZona("Opción");

                switch (op) {
                    case 'a':
                        zona.setTipoVehiculo(TipoVehiculo.MOTORHOME.name());
                        zona.setAncho(LIMITE_AMH);
                        zona.setLargo(LIMITE_LMH);
                        zona.setCapacidadVehiculos(LIMITE_CAPACIDAD2);
                        break;
                    case 'b':
                        zona.setTipoVehiculo(TipoVehiculo.CASA_RODANTE.name());
                        zona.setAncho(LIMITE_ACR);
                        zona.setLargo(LIMITE_LCR);
                        zona.setCapacidadVehiculos(LIMITE_CAPACIDAD1);
                        break;
                    case 'c':
                        zona.setTipoVehiculo(TipoVehiculo.CARAVANA.name());
                        zona.setAncho(LIMITE_ACA);
                        zona.setLargo(LIMITE_LCA);
                        zona.setCapacidadVehiculos(LIMITE_CAPACIDAD2);
                        break;
                    case 'd':
                        zona.setTipoVehiculo(TipoVehiculo.TRAILER.name());
                        zona.setAncho(LIMITE_ATR);
                        zona.setLargo(LIMITE_LTR);
                        zona.setCapacidadVehiculos(LIMITE_CAPACIDAD1);
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        return;
                }
                break;

            default:
                System.out.println("Opción inválida.");
                return;
        }

        // 🔹 Persistimos el DTO actualizado
        try {
            adminController.modificarZona(zona);
            System.out.println("Zona actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar zona: " + e.getMessage());
        }
    }

    private void eliminarZona() {
        System.out.println("--- Lista de Zonas ---");
        List<ZonaDTO> zonas = adminController.listarTodasLasZonas();
        for (ZonaDTO z : zonas) {
            System.out.println("ID: " + z.getId() + " | Letra: " + z.getLetra()
                    + " | Tipo: " + z.getTipoVehiculo()
                    + " | Capacidad: " + z.getCapacidadVehiculos());
        }

        int idZona = leerNumeroPositivo("Ingrese el ID de la zona a eliminar");

        // Buscar zona en la lista
        ZonaDTO zonaAEliminar = null;
        for (ZonaDTO z : zonas) {
            if (z.getId() == idZona) {
                zonaAEliminar = z;
                break;
            }
        }

        if (zonaAEliminar == null) {
            System.out.println("No se encontró ninguna zona con ese ID.");
            return;
        }

        try {
            // 🔹 Eliminamos por letra, que es lo que espera el service
            adminController.eliminarZonaPorLetra(zonaAEliminar.getLetra());
            System.out.println("Zona eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar zona: " + e.getMessage());
        }
    }

    private void altaVehiculo() {
        // --- 1. Selección de socio propietario ---
        System.out.println("--- Lista de Socios ---");
        List<SocioDTO> socios = adminController.listarTodosLosSocios();
        for (SocioDTO s : socios) {
            System.out.println("ID: " + s.getId() + " | Nombre: " + s.getNombre() + " | DNI: " + s.getDni());
        }

        int socioId = leerNumeroPositivo("Ingrese ID del socio propietario");
        SocioDTO socioPropietario = null;
        for (SocioDTO s : socios) {
            if (s.getId() == socioId) {
                socioPropietario = s;
                break;
            }
        }

        if (socioPropietario == null) {
            System.out.println("Error: no existe un socio con ese ID.");
            return;
        }

        // --- 2. Nombre del vehículo ---
        String nombre = leerTextoConLimite("Ingrese nombre del vehículo", LIMITE_USUARIO);

        // --- 3. Validación de matrícula ---
        String matricula;
        boolean matriculaValida = false;
        do {
            matricula = leerTexto("Ingrese matrícula (alfanumérica, máx. 7 caracteres)");
            if (!matricula.matches("[A-Za-z0-9]{1,7}")) {
                System.out.println("Error: matrícula inválida (máx 7 caracteres alfanuméricos).");
            } else {
                // Validar que no esté duplicada en la lista de vehículos
                List<VehiculoDTO> vehiculos = adminController.listarTodosLosVehiculos();
                boolean duplicada = false;
                for (VehiculoDTO v : vehiculos) {
                    if (v.getMatricula().equalsIgnoreCase(matricula)) {
                        duplicada = true;
                        break;
                    }
                }
                if (duplicada) {
                    System.out.println("Error: ya existe un vehículo con matrícula " + matricula);
                } else {
                    matriculaValida = true;
                }
            }
        } while (!matriculaValida);

        // --- 4. Selección de tipo y límites ---
        System.out.println("Seleccione tipo: a. Motorhome / b. Casa Rodante / c. Caravana / d. Trailer");
        char opcionTipo = leerLetraZona("Opción");
        TipoVehiculo tipo;
        float maxLargo, maxAncho;

        switch (opcionTipo) {
            case 'a':
                tipo = TipoVehiculo.MOTORHOME;
                maxLargo = LIMITE_LMH;
                maxAncho = LIMITE_AMH;
                break;
            case 'b':
                tipo = TipoVehiculo.CASA_RODANTE;
                maxLargo = LIMITE_LCR;
                maxAncho = LIMITE_ACR;
                break;
            case 'c':
                tipo = TipoVehiculo.CARAVANA;
                maxLargo = LIMITE_LCA;
                maxAncho = LIMITE_ACA;
                break;
            case 'd':
                tipo = TipoVehiculo.TRAILER;
                maxLargo = LIMITE_LTR;
                maxAncho = LIMITE_ATR;
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        // --- 5. Lectura de dimensiones con validación ---
        float profundidad = (float) leerMedida("profundidad", maxLargo);
        float ancho = (float) leerMedida("ancho", maxAncho);

        // --- 6. Registro mediante DTO ---
        VehiculoDTO nuevoVehiculo = new VehiculoDTO(
                0, // ID inicial
                socioPropietario.getId(), // ID socio propietario
                0, // ID garage (si aplica, inicial en 0)
                nombre,
                matricula,
                tipo.name(), // tipo como String
                profundidad,
                ancho
        );

        try {
            adminController.registrarVehiculo(nuevoVehiculo);
            System.out.println("Vehículo registrado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al registrar vehículo: " + e.getMessage());
        }
    }

// Método auxiliar para evitar repetición de código en las medidas
    private double leerMedida(String campo, float limite) {
        double valor = -1;
        while (valor < 0 || valor > limite) {
            valor = leerNumeroPositivoDouble("Ingrese " + campo + " (máx. " + limite + ")");
            if (valor > limite) {
                System.out.println("Error: supera el límite permitido.");
            }
        }
        return valor;
    }

    private void modificarVehiculo(int idVehiculo) throws ErrorNegocio {
        System.out.println("--- Lista de Vehículos ---");
        List<VehiculoDTO> vehiculos = adminController.listarTodosLosVehiculos();
        for (VehiculoDTO v : vehiculos) {
            System.out.println("ID: " + v.getId() + " | Nombre: " + v.getNombre()
                    + " | Matrícula: " + v.getMatricula()
                    + " | Socio: " + v.getSocioId()
                    + " | Tipo: " + v.getTipo());
        }

        // Buscar vehículo en la lista
        VehiculoDTO vehiculo = null;
        for (VehiculoDTO v : vehiculos) {
            if (v.getId() == idVehiculo) {
                vehiculo = v;
                break;
            }
        }

        if (vehiculo == null) {
            System.out.println("No se encontró ningún vehículo con ese ID.");
            return;
        }

        System.out.println("Modificando vehículo: " + vehiculo.getNombre());
        System.out.println("a. Nombre / b. Matrícula / c. Socio propietario / d. Tipo y dimensiones");
        char opcion = leerLetraZona("Opción");

        switch (opcion) {
            case 'a':
                vehiculo.setNombre(leerTextoConLimite("Nuevo nombre", LIMITE_USUARIO));
                break;

            case 'b':
                // 🔹 El menú solo pide la nueva matrícula, sin validar aquí
                String nuevaMatricula = leerTexto("Nueva matrícula (alfanumérica, máx. 7)");
                vehiculo.setMatricula(nuevaMatricula);
                break;

            case 'c':
                System.out.println("--- Lista de Socios ---");
                List<SocioDTO> socios = adminController.listarTodosLosSocios();
                for (SocioDTO s : socios) {
                    System.out.println("ID: " + s.getId() + " | Nombre: " + s.getNombre());
                }

                int nuevoSocioId = leerNumeroPositivo("ID nuevo socio");
                SocioDTO socio = null;
                for (SocioDTO s : socios) {
                    if (s.getId() == nuevoSocioId) {
                        socio = s;
                        break;
                    }
                }

                if (socio != null) {
                    vehiculo.setSocioId(socio.getId());
                } else {
                    System.out.println("Error: socio no encontrado.");
                }
                break;

            case 'd':
                System.out.println("Seleccione: a. Motorhome, b. Casa Rodante, c. Caravana, d. Trailer");
                char op = leerLetraZona("Opción");
                TipoVehiculo t;
                float pMax,
                 aMax;

                switch (op) {
                    case 'a':
                        t = TipoVehiculo.MOTORHOME;
                        pMax = LIMITE_LMH;
                        aMax = LIMITE_AMH;
                        break;
                    case 'b':
                        t = TipoVehiculo.CASA_RODANTE;
                        pMax = LIMITE_LCR;
                        aMax = LIMITE_ACR;
                        break;
                    case 'c':
                        t = TipoVehiculo.CARAVANA;
                        pMax = LIMITE_LCA;
                        aMax = LIMITE_ACA;
                        break;
                    case 'd':
                        t = TipoVehiculo.TRAILER;
                        pMax = LIMITE_LTR;
                        aMax = LIMITE_ATR;
                        break;
                    default:
                        System.out.println("Inválido.");
                        return;
                }

                vehiculo.setTipo(t.name());
                vehiculo.setProfundidad((float) leerMedida("profundidad", pMax));
                vehiculo.setAncho((float) leerMedida("ancho", aMax));
                break;

            default:
                System.out.println("Opción inválida.");
                return;
        }

        // 🔹 Persistimos el DTO actualizado
        adminController.modificarVehiculo(vehiculo);
        System.out.println("Vehículo actualizado correctamente.");

    }

    private void eliminarVehiculo() {
        System.out.println("--- Lista de Vehículos ---");
        // Se mantiene la llamada para que el usuario visualice la lista actual
        adminController.listarTodosLosVehiculos();

        int idVehiculo = leerNumeroPositivo("Ingrese el ID del vehículo a eliminar");

        // Validamos la existencia utilizando el controlador antes de proceder
        VehiculoDTO vehiculo = adminController.buscarVehiculoPorId(idVehiculo);

        if (vehiculo == null) {
            System.out.println("No se encontró ningún vehículo con ese ID.");
            return;
        }

        // Solicitamos al controlador la eliminación mediante el ID
        adminController.eliminarVehiculo(idVehiculo);

        System.out.println("Vehículo eliminado correctamente.");
    }
}
