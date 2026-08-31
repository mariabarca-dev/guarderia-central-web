package controller;

import model.Usuario;
import model.Rol;
import service.AdministradorService;
import service.EmpleadoService;
import service.SocioService;
import dto.AdministradorDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;

import java.util.List;

public class SuperAdminController implements Controlador {

    private final Usuario usuarioLogueado;
    private final AdministradorService adminService = new AdministradorService();
    private final EmpleadoService empleadoService = new EmpleadoService();
    private final SocioService socioService = new SocioService();

    public SuperAdminController(Usuario usuario) {
        if (usuario == null || usuario.getRol() != Rol.SUPERADMINISTRADOR) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de Super Administrador.");
        }
        this.usuarioLogueado = usuario;
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
        // Implementación exigida por la interfaz Controlador
    }

    // ==========================================
    // ABM ADMINISTRADORES
    // ==========================================
    public void registrarAdministrador(AdministradorDTO dto) throws ErrorNegocio {
        adminService.registrarAdministrador(dto);
    }

    public List<AdministradorDTO> listarAdministradores() {
        return adminService.listarTodos();
    }

    public AdministradorDTO buscarAdministradorPorId(int id) {
        try {
            return adminService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public void actualizarAdministrador(AdministradorDTO dto) throws ErrorNegocio, RegistroNoEncontradoException {
        adminService.actualizarAdministrador(dto);
    }

    public void eliminarAdministrador(int id) throws RegistroNoEncontradoException {
        adminService.eliminarAdministrador(id);
    }

    // ==========================================
    // ABM EMPLEADOS
    // ==========================================
    public void registrarEmpleado(EmpleadoDTO dto) throws ErrorNegocio {
        empleadoService.registrarEmpleado(dto);
    }

    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoService.listarTodos();
    }

    public void actualizarEmpleado(EmpleadoDTO dto) throws RegistroNoEncontradoException {
        empleadoService.actualizarEmpleado(dto);
    }

    public void eliminarEmpleado(int id) throws RegistroNoEncontradoException {
        empleadoService.eliminarEmpleado(id);
    }

    // ==========================================
    // ABM SOCIOS
    // ==========================================
    public void registrarSocio(SocioDTO dto) throws ErrorNegocio {
        socioService.registrarSocio(dto);
    }

    public List<SocioDTO> listarSocios() {
        return socioService.listarTodos();
    }

    public SocioDTO buscarSocioPorId(int id) {
        try {
            return socioService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public void actualizarSocio(SocioDTO dto) throws RegistroNoEncontradoException {
        socioService.actualizarSocio(dto);
    }

    public void eliminarSocio(int id) throws RegistroNoEncontradoException {
        socioService.eliminarSocio(id);
    }
}