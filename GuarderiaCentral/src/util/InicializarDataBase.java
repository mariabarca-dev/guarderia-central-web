package util;

import dto.AdministradorDTO;
import dto.EmpleadoDTO;
import dto.SocioDTO;
import exceptions.ErrorNegocio;
import java.time.LocalDate;
import model.Rol;
import service.AdministradorService;
import service.EmpleadoService;
import service.SocioService;

public class InicializarDataBase {
    private final AdministradorService adminService;
    private final EmpleadoService empleadoService;
    private final SocioService socioService;

    public InicializarDataBase(AdministradorService adminService, 
                               EmpleadoService empleadoService, 
                               SocioService socioService) {
        this.adminService = adminService;
        this.empleadoService = empleadoService;
        this.socioService = socioService;
    }

    public void verificarConfiguracionInicial() throws ErrorNegocio {
        // Inicializar Administrador (Usando ID base 100)
        if (adminService.listarTodos().isEmpty()) {
            adminService.registrarAdministrador(new AdministradorDTO(
                100, "Admin Default", "Calle Falsa 123", "12345678", "admin", "admin123", Rol.ADMINISTRADOR
            ));
        }

        // Inicializar Empleado (Usando ID base 300)
        if (empleadoService.listarTodos().isEmpty()) {
            empleadoService.registrarEmpleado(new EmpleadoDTO(
                300, "Empleado Default", "Av. Principal 456", "87654321", "EMP001", "empleado123", Rol.EMPLEADO, "EMP-01", "Cuidacoche"
            ));
        }

        // Inicializar Socio (Usando ID base 600)
        if (socioService.listarTodos().isEmpty()) {
            socioService.registrarSocio(new SocioDTO(
                600, "Socio Default", "Calle Real 789", "11223344", "socio", "socio123", Rol.SOCIO, "46598674", LocalDate.now()
            ));
        }
    }
}