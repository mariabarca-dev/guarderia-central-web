package controller;

import model.Usuario;
import service.UsuarioService;
import service.EmpleadoService;
import service.SocioService;
import mapper.UsuarioMapper;
import mapper.EmpleadoMapper;
import mapper.SocioMapper;
// CORREGIDO: Se cambió view2 por view.impl
import view.impl.MenuAdminImpl;
import view.impl.MenuEmpleadoImpl;
import view.impl.MenuSocioImpl;
import dto.UsuarioDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;

///estoy probando el intellig idea

public class LoginController {
    
    // Inyectamos servicios necesarios
    private UsuarioService usuarioService;
    private EmpleadoService empleadoService;
    private SocioService socioService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.empleadoService = new EmpleadoService();
        this.socioService = new SocioService();
    }

    public void login(String nombreUsuario, String claveIngresada) {
        try {
            // 1. Buscamos al usuario (autenticación) y obtenemos el DTO (Corregido: el service retorna UsuarioDTO)
            UsuarioDTO usuarioDto = usuarioService.buscarPorNombreUsuario(nombreUsuario);
            
            if (usuarioDto == null || !usuarioDto.getClave().equals(claveIngresada)) {
                System.out.println("Error: Usuario o clave incorrectos.");
                return;
            }

            // CORREGIDO: Convertimos el DTO a Modelo para usarlo en la lógica del controlador y RBAC
            // UsuarioMapper.toModel(UsuarioDTO) funciona porque delega a los mappers específicos si detecta la instancia
            Usuario usuarioModel = UsuarioMapper.toModel(usuarioDto);

            System.out.println("Bienvenido, " + usuarioModel.getNombre());

            // 2. Selección de flujo basada en ROL
            switch (usuarioModel.getRol()) {
                case ADMINISTRADOR:
                    // El AdminController maneja el modelo directamente (ya que es usuarioModel)
                    AdminController adminCtrl = new AdminController(usuarioModel);
                    new MenuAdminImpl(adminCtrl).mostrar();
                    break;

                case EMPLEADO:
                    // CORREGIDO: Recuperamos el modelo específico casteando desde Usuario
                    // Asumimos que si el rol es EMPLEADO, la instancia es model.Empleado
                    model.Empleado empleadoModelo = (model.Empleado) usuarioModel;
                        
                    // Convertimos el modelo a DTO para la vista usando EmpleadoMapper
                    EmpleadoDTO empDto = EmpleadoMapper.toDto(empleadoModelo);
                    
                    EmpleadoController empCtrl = new EmpleadoController(usuarioModel);
                    new MenuEmpleadoImpl(empCtrl, empDto).mostrar();
                    break;

                case SOCIO:
                    // CORREGIDO: Recuperamos el modelo específico casteando desde Usuario
                    model.Socio socioModelo = (model.Socio) usuarioModel;
                        
                    // Convertimos el modelo a DTO para la vista usando SocioMapper
                    SocioDTO socioDto = SocioMapper.toDto(socioModelo);
                    
                    // Nota: en tu código original tenías "socioCtrl = new SocioController(usuario)",
                    // asumo que el constructor de SocioController también acepta el modelo Usuario.
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