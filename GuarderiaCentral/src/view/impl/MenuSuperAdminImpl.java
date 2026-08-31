package view.impl;

import controller.SuperAdminController;
import dto.AdministradorDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;
import model.Rol;

public class MenuSuperAdminImpl extends VistaImpl {

    private final SuperAdminController controller;

    public MenuSuperAdminImpl(SuperAdminController controller) {
        this.controller = controller;
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
                    for (AdministradorDTO a : controller.listarAdministradores()) {
                        System.out.println("ID: " + a.getId() + " | Usuario: " + a.getNombreUsuario() + " | Nombre: " + a.getNombre());
                    }
                    break;
                case 2:
                    AdministradorDTO dtoReg = new AdministradorDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setNombreUsuario(leerTexto("Nombre de usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.ADMINISTRADOR);
                    controller.registrarAdministrador(dtoReg);
                    System.out.println("Administrador registrado con éxito.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del administrador a modificar");
                    AdministradorDTO dtoMod = new AdministradorDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.ADMINISTRADOR);
                    controller.actualizarAdministrador(dtoMod);
                    System.out.println("Administrador modificado con éxito.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del administrador a eliminar");
                    controller.eliminarAdministrador(idElim);
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
                    for (EmpleadoDTO e : controller.listarEmpleados()) {
                        System.out.println("ID: " + e.getId() + " | Código: " + e.getCodigo() + " | Nombre: " + e.getNombre());
                    }
                    break;
                case 2:
                    EmpleadoDTO dtoReg = new EmpleadoDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setCodigo(leerTexto("Código del empleado"));
                    dtoReg.setNombreUsuario(leerTexto("Usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.EMPLEADO);
                    controller.registrarEmpleado(dtoReg);
                    System.out.println("Empleado registrado correctamente.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del empleado a modificar");
                    EmpleadoDTO dtoMod = new EmpleadoDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setCodigo(leerTexto("Nuevo Código del empleado"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.EMPLEADO);
                    controller.actualizarEmpleado(dtoMod);
                    System.out.println("Empleado modificado correctamente.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del empleado a eliminar");
                    controller.eliminarEmpleado(idElim);
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
                    for (SocioDTO s : controller.listarSocios()) {
                        System.out.println("ID: " + s.getId() + " | DNI: " + s.getDni() + " | Nombre: " + s.getNombre());
                    }
                    break;
                case 2:
                    SocioDTO dtoReg = new SocioDTO();
                    dtoReg.setNombre(leerTexto("Nombre"));
                    dtoReg.setDni(leerTexto("DNI"));
                    dtoReg.setNombreUsuario(leerTexto("Usuario"));
                    dtoReg.setClave(leerTexto("Contraseña"));
                    dtoReg.setRol(Rol.SOCIO);
                    controller.registrarSocio(dtoReg);
                    System.out.println("Socio registrado correctamente.");
                    break;
                case 3:
                    int idMod = leerEntero("ID del socio a modificar");
                    SocioDTO dtoMod = new SocioDTO();
                    dtoMod.setId(idMod);
                    dtoMod.setNombre(leerTexto("Nuevo Nombre"));
                    dtoMod.setDni(leerTexto("Nuevo DNI"));
                    dtoMod.setNombreUsuario(leerTexto("Nuevo Usuario"));
                    dtoMod.setClave(leerTexto("Nueva Contraseña"));
                    dtoMod.setRol(Rol.SOCIO);
                    controller.actualizarSocio(dtoMod);
                    System.out.println("Socio modificado correctamente.");
                    break;
                case 4:
                    int idElim = leerEntero("ID del socio a eliminar");
                    controller.eliminarSocio(idElim);
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