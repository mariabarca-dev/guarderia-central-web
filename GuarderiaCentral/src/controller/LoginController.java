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
import view.impl.MenuSuperAdminImpl; // <--- Import del menú SuperAdmin
import dto.UsuarioDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;

public class LoginController implements Controlador {

    // Inyectamos servicios necesarios
    private UsuarioService usuarioService;
    private EmpleadoService empleadoService;
    private SocioService socioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.empleadoService = new EmpleadoService();
        this.socioService = new SocioService();
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        try {
            // 1. Buscamos al usuario (autenticación) y obtenemos el DTO
            UsuarioDTO usuarioDto = usuarioService.buscarPorNombreUsuario(nombreUsuario);

            if (usuarioDto == null || !usuarioDto.getClave().equals(claveIngresada)) {
                System.out.println("Error: Usuario o clave incorrectos.");
                return;
            }

            // Convertimos el DTO a Modelo para usarlo en la lógica del controlador y RBAC
            Usuario usuarioModel = UsuarioMapper.toModel(usuarioDto);

            System.out.println("Bienvenido, " + usuarioModel.getNombre());

            // 2. Selección de flujo basada en ROL
            switch (usuarioModel.getRol()) {
                case SUPERADMINISTRADOR:
                    SuperAdminController superCtrl = new SuperAdminController(usuarioModel);
                    new MenuSuperAdminImpl(superCtrl).mostrar();
                    break;

                case ADMINISTRADOR:
                    AdminController adminCtrl = new AdminController(usuarioModel);
                    new MenuAdminImpl(adminCtrl).mostrar();
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
        } catch (Exception e) {
            System.out.println("Ocurrió un error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}