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
        System.out.println("3. Eliminar Administrador");
        int op = leerEntero("Seleccione");

        try {
            if (op == 1) {
                for (AdministradorDTO a : controller.listarAdministradores()) {
                    System.out.println("ID: " + a.getId() + " | Usuario: " + a.getNombreUsuario() + " | Nombre: " + a.getNombre());
                }
            } else if (op == 2) {
                AdministradorDTO dto = new AdministradorDTO();
                dto.setNombre(leerTexto("Nombre"));
                dto.setNombreUsuario(leerTexto("Nombre de usuario"));
                dto.setClave(leerTexto("Contraseña"));
                dto.setRol(Rol.ADMINISTRADOR);
                controller.registrarAdministrador(dto);
                System.out.println("Administrador registrado con éxito.");
            } else if (op == 3) {
                int id = leerEntero("ID del administrador a eliminar");
                controller.eliminarAdministrador(id);
                System.out.println("Administrador eliminado.");
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
        System.out.println("3. Eliminar Empleado");
        int op = leerEntero("Seleccione");

        try {
            if (op == 1) {
                for (EmpleadoDTO e : controller.listarEmpleados()) {
                    System.out.println("ID: " + e.getId() + " | Código: " + e.getCodigo() + " | Nombre: " + e.getNombre());
                }
            } else if (op == 2) {
                EmpleadoDTO dto = new EmpleadoDTO();
                dto.setNombre(leerTexto("Nombre"));
                dto.setCodigo(leerTexto("Código del empleado"));
                dto.setNombreUsuario(leerTexto("Usuario"));
                dto.setClave(leerTexto("Contraseña"));
                dto.setRol(Rol.EMPLEADO);
                controller.registrarEmpleado(dto);
                System.out.println("Empleado registrado correctamente.");
            } else if (op == 3) {
                int id = leerEntero("ID del empleado a eliminar");
                controller.eliminarEmpleado(id);
                System.out.println("Empleado eliminado.");
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
        System.out.println("3. Eliminar Socio");
        int op = leerEntero("Seleccione");

        try {
            if (op == 1) {
                for (SocioDTO s : controller.listarSocios()) {
                    System.out.println("ID: " + s.getId() + " | DNI: " + s.getDni() + " | Nombre: " + s.getNombre());
                }
            } else if (op == 2) {
                SocioDTO dto = new SocioDTO();
                dto.setNombre(leerTexto("Nombre"));
                dto.setDni(leerTexto("DNI"));
                dto.setNombreUsuario(leerTexto("Usuario"));
                dto.setClave(leerTexto("Contraseña"));
                dto.setRol(Rol.SOCIO);
                controller.registrarSocio(dto);
                System.out.println("Socio registrado correctamente.");
            } else if (op == 3) {
                int id = leerEntero("ID del socio a eliminar");
                controller.eliminarSocio(id);
                System.out.println("Socio eliminado.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        presionarParaContinuar();
    }
}