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
import view.impl.MenuSuperAdminImpl;
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

            UsuarioDTO usuarioDto = usuarioService.buscarPorNombreUsuario(nombreUsuario);

            if (usuarioDto == null || !usuarioDto.getClave().equals(claveIngresada)) {
                System.out.println("Error: Usuario o clave incorrectos.");
                return;
            }

            Usuario usuarioModel = UsuarioMapper.toModel(usuarioDto);
            System.out.println("\nBienvenido, " + usuarioModel.getNombre() + "!");

            switch (usuarioModel.getRol()) {
                case SUPERADMINISTRADOR:
                    SuperAdminController superCtrl = new SuperAdminController(usuarioModel);
                    new MenuSuperAdminImpl(superCtrl).mostrar();
                    break;

                case ADMINISTRADOR:
                    // Instanciación corregida: se pasa solo el usuario al constructor del controlador (o vacío según corresponda)
                    AdminController adminCtrl = new AdminController(usuarioModel);
                    AsignacionEmpleadoZonaController asigEmpZonaCtrl = new AsignacionEmpleadoZonaController(usuarioModel);
                    AsignacionVehiculoGarageController asigVehGarCtrl = new AsignacionVehiculoGarageController(usuarioModel);
                    EmpleadoController empCtrlAdmin = new EmpleadoController(usuarioModel);
                    SocioController socioCtrlAdmin = new SocioController(usuarioModel);
                    VehiculoController vehCtrlAdmin = new VehiculoController(usuarioModel);

                    // Controladores con constructores vacíos (sin servicios ni usuario por parámetro)
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

                    EmpleadoController empCtrl = new EmpleadoController(usuarioModel);
                    new MenuEmpleadoImpl(empCtrl, empDto).mostrar();
                    break;

                case SOCIO:
                    model.Socio socioModelo = (model.Socio) usuarioModel;
                    SocioDTO socioDto = SocioMapper.toDto(socioModelo);

                    SocioController socioCtrl = new SocioController(usuarioModel);
                    new MenuSocioImpl(socioCtrl, socioDto).mostrar();
                    break;

                default:
                    System.out.println("Error: Rol no reconocido en el sistema.");
                    break;
            }

        } catch (SecurityException e) {
            System.out.println("Acceso denegado: " + e.getMessage());
        } catch (ErrorNegocio e) {
            System.out.println("Error de autenticación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado al iniciar sesión. Intente nuevamente.");
        }
    }
}