package controller;

import model.Usuario;
import service.UsuarioService;
import service.EmpleadoService;
import service.SocioService;
import mapper.UsuarioMapper;
import mapper.EmpleadoMapper;
import mapper.SocioMapper;
import view.impl.MenuAdminImpl;
import view.impl.MenuEmpleadoImpl;
import view.impl.MenuSocioImpl;
import view.impl.MenuSysAdminImpl;
import dto.UsuarioDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;
import exceptions.*;

public class LoginController implements Controlador {

    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;
    private final SocioService socioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.empleadoService = new EmpleadoService();
        this.socioService = new SocioService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        try {
            if (nombreUsuario == null || nombreUsuario.isBlank() || claveIngresada == null || claveIngresada.isBlank()) {
                System.out.println("Error: El nombre de usuario y la contraseña no pueden estar vacíos.");
                return;
            }

            // Validar login utilizando el servicio y lanzando CredencialesInvalidasException si falla
            Usuario usuarioModel = usuarioService.validarLogin(nombreUsuario, claveIngresada);

            System.out.println("\nBienvenido, " + usuarioModel.getNombre() + "!");

            switch (usuarioModel.getRol()) {
                case SYS_ADMIN:
                    AdminController adminCtrlSys = new AdminController();
                    EmpleadoController empCtrlSys = new EmpleadoController();
                    SocioController socioCtrlSys = new SocioController();

                    new MenuSysAdminImpl(
                            adminCtrlSys,
                            empCtrlSys,
                            socioCtrlSys,
                            usuarioModel
                    ).mostrar();
                    break;

                case ADMINISTRADOR:
                    AdminController adminCtrl = new AdminController();
                    AsignacionEmpleadoZonaController asigEmpZonaCtrl = new AsignacionEmpleadoZonaController();
                    AsignacionVehiculoGarageController asigVehGarCtrl = new AsignacionVehiculoGarageController();
                    EmpleadoController empCtrlAdmin = new EmpleadoController();
                    SocioController socioCtrlAdmin = new SocioController();
                    VehiculoController vehCtrlAdmin = new VehiculoController();
                    GarageController garageCtrlAdmin = new GarageController();
                    PropiedadGarageController propGarCtrlAdmin = new PropiedadGarageController();
                    ZonaController zonaCtrlAdmin = new ZonaController();

                    new MenuAdminImpl(
                            adminCtrl,
                            asigEmpZonaCtrl,
                            asigVehGarCtrl,
                            empCtrlAdmin,
                            socioCtrlAdmin,
                            vehCtrlAdmin,
                            garageCtrlAdmin,
                            propGarCtrlAdmin,
                            zonaCtrlAdmin,
                            usuarioModel
                    ).mostrar();
                    break;

                case EMPLEADO:
                    model.Empleado empleadoModelo = (model.Empleado) usuarioModel;
                    EmpleadoDTO empDto = EmpleadoMapper.toDto(empleadoModelo);

                    EmpleadoController empCtrl = new EmpleadoController();
                    new MenuEmpleadoImpl(empCtrl, empDto, usuarioModel).mostrar();
                    break;

                case SOCIO:
                    model.Socio socioModelo = (model.Socio) usuarioModel;
                    SocioDTO socioDto = SocioMapper.toDto(socioModelo);

                    SocioController socioCtrl = new SocioController();
                    new MenuSocioImpl(socioCtrl, socioDto, usuarioModel).mostrar();
                    break;

                default:
                    System.out.println("Error: Rol no reconocido en el sistema.");
                    break;
            }

        } catch (CredencialesInvalidasException e) {
            System.out.println("Error de autenticación: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("Acceso denegado: " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de negocio: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado al iniciar sesión. Intente nuevamente.");
        }
    }
}