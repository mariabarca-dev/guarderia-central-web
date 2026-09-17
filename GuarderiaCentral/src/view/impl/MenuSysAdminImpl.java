package view.impl;

import controller.AdminController;
import controller.EmpleadoController;
import controller.SocioController;
import dto.AdministradorDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;
import dto.UsuarioDTO;
import model.Rol;
import model.Usuario;
import view.MenuSysAdmin;

public class MenuSysAdminImpl extends VistaImpl implements MenuSysAdmin {

    private final AdminController adminController;
    private final EmpleadoController empleadoController;
    private final SocioController socioController;
    private final Usuario usuarioSesion;

    public MenuSysAdminImpl(AdminController adminController,
                            EmpleadoController empleadoController,
                            SocioController socioController,
                            Usuario usuarioSesion) {
        this.adminController = adminController;
        this.empleadoController = empleadoController;
        this.socioController = socioController;
        this.usuarioSesion = usuarioSesion;
    }

    @Override
    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            imprimirEncabezado("MENÚ SUPER ADMINISTRADOR - GESTIÓN DE ROLES");
            System.out.println("1. Gestión de Administradores");
            System.out.println("2. Gestión de Empleados");
            System.out.println("3. Gestión de Socios");
            System.out.println("0. Cerrar Sesión");

            opcion = leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    menuAdministradores();
                    break;
                case 2:
                    menuEmpleados();
                    break;
                case 3:
                    menuSocios();
                    break;
                case 0:
                    System.out.println("Cerrando sesión de Super Administrador...");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    presionarParaContinuar();
                    break;
            }
        }
    }

    // ----------------------------------------------------
    // SUBMENÚ ADMINISTRADORES
    // ----------------------------------------------------
    private void menuAdministradores() {
        imprimirEncabezado("ABM ADMINISTRADORES");
        System.out.println("1. Listar Administradores");
        System.out.println("2. Registrar Administrador");
        System.out.println("3. Modificar Administrador");
        System.out.println("4. Eliminar Administrador");
        System.out.println("0. Volver al Menú Principal");

        int op = leerEntero("Seleccione");
        if (op == 0) return;

        try {
            switch (op) {
                case 1:
                    for (UsuarioDTO u : adminController.listarTodosLosUsuarios(usuarioSesion)) {
                        if (u instanceof AdministradorDTO a) {
                            System.out.println("ID: " + a.getId() + " | Usuario: " + a.getNombreUsuario() + " | Nombre: " + a.getNombre());
                        }
                    }
                    break;
                case 2:
                    AdministradorDTO dtoReg = new AdministradorDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setApellido(leerTexto("Apellido"));
                    dtoReg.setDireccion(leerTexto("Dirección"));
                    dtoReg.setTelefono(leerTexto("Teléfono"));
                    dtoReg.setNombreUsuario(leerTexto("Nombre de usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.ADMINISTRADOR);

                    adminController.registrarAdministrador(usuarioSesion, dtoReg);
                    System.out.println("Administrador registrado con éxito.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del administrador a modificar");
                    AdministradorDTO dtoMod = new AdministradorDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setApellido(leerTexto("Nuevo Apellido"));
                    dtoMod.setDireccion(leerTexto("Nueva Dirección"));
                    dtoMod.setTelefono(leerTexto("Nuevo Teléfono"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.ADMINISTRADOR);

                    adminController.modificarUsuario(usuarioSesion, dtoMod);
                    System.out.println("Administrador modificado con éxito.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del administrador a eliminar");
                    adminController.eliminarUsuario(usuarioSesion, idElim);
                    System.out.println("Administrador eliminado.");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        presionarParaContinuar();
    }

    // ----------------------------------------------------
    // SUBMENÚ EMPLEADOS
    // ----------------------------------------------------
    private void menuEmpleados() {
        imprimirEncabezado("ABM EMPLEADOS");
        System.out.println("1. Listar Empleados");
        System.out.println("2. Registrar Empleado");
        System.out.println("3. Modificar Empleado");
        System.out.println("4. Eliminar Empleado");
        System.out.println("0. Volver al Menú Principal");

        int op = leerEntero("Seleccione");
        if (op == 0) return;

        try {
            switch (op) {
                case 1:
                    for (UsuarioDTO u : adminController.listarTodosLosUsuarios(usuarioSesion)) {
                        if (u instanceof EmpleadoDTO e) {
                            System.out.println("ID: " + e.getId() + " | Código: " + e.getCodigo() + " | Nombre: " + e.getNombre());
                        }
                    }
                    break;
                case 2:
                    EmpleadoDTO dtoReg = new EmpleadoDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setApellido(leerTexto("Apellido"));
                    dtoReg.setDireccion(leerTexto("Dirección"));
                    dtoReg.setTelefono(leerTexto("Teléfono"));
                    dtoReg.setCodigo(leerTexto("Código del empleado"));
                    dtoReg.setEspecialidad(leerTexto("Especialidad"));
                    dtoReg.setNombreUsuario(leerTexto("Usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.EMPLEADO);

                    empleadoController.registrarEmpleado(usuarioSesion, dtoReg);
                    System.out.println("Empleado registrado correctamente.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del empleado a modificar");
                    EmpleadoDTO dtoMod = new EmpleadoDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setApellido(leerTexto("Nuevo Apellido"));
                    dtoMod.setDireccion(leerTexto("Nueva Dirección"));
                    dtoMod.setTelefono(leerTexto("Nuevo Teléfono"));
                    dtoMod.setCodigo(leerTexto("Nuevo Código del empleado"));
                    dtoMod.setEspecialidad(leerTexto("Nueva Especialidad"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.EMPLEADO);

                    adminController.modificarUsuario(usuarioSesion, dtoMod);
                    System.out.println("Empleado modificado correctamente.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del empleado a eliminar");
                    adminController.eliminarUsuario(usuarioSesion, idElim);
                    System.out.println("Empleado eliminado.");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        presionarParaContinuar();
    }

    // ----------------------------------------------------
    // SUBMENÚ SOCIOS
    // ----------------------------------------------------
    private void menuSocios() {
        imprimirEncabezado("ABM SOCIOS");
        System.out.println("1. Listar Socios");
        System.out.println("2. Registrar Socio");
        System.out.println("3. Modificar Socio");
        System.out.println("4. Eliminar Socio");
        System.out.println("0. Volver al Menú Principal");

        int op = leerEntero("Seleccione");
        if (op == 0) return;

        try {
            switch (op) {
                case 1:
                    for (UsuarioDTO u : adminController.listarTodosLosUsuarios(usuarioSesion)) {
                        if (u instanceof SocioDTO s) {
                            System.out.println("ID: " + s.getId() + " | DNI: " + s.getDni() + " | Nombre: " + s.getNombre());
                        }
                    }
                    break;
                case 2:
                    SocioDTO dtoReg = new SocioDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setApellido(leerTexto("Apellido"));
                    dtoReg.setDireccion(leerTexto("Dirección"));
                    dtoReg.setTelefono(leerTexto("Teléfono"));
                    dtoReg.setDni(leerTexto("DNI"));
                    dtoReg.setNombreUsuario(leerTexto("Usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.SOCIO);

                    adminController.modificarUsuario(usuarioSesion, dtoReg); // o a través del controlador correspondiente
                    System.out.println("Socio registrado correctamente.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del socio a modificar");
                    SocioDTO dtoMod = new SocioDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setApellido(leerTexto("Nuevo Apellido"));
                    dtoMod.setDireccion(leerTexto("Nueva Dirección"));
                    dtoMod.setTelefono(leerTexto("Nuevo Teléfono"));
                    dtoMod.setDni(leerTexto("Nuevo DNI"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.SOCIO);

                    adminController.modificarUsuario(usuarioSesion, dtoMod);
                    System.out.println("Socio modificado correctamente.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del socio a eliminar");
                    adminController.eliminarUsuario(usuarioSesion, idElim);
                    System.out.println("Socio eliminado.");
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        presionarParaContinuar();
    }
}