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
        // Inicializar SuperAdministrador (Usando ID base 1)
        boolean existeSuperAdmin = adminService.listarTodos().stream()
                .anyMatch(a -> a.getRol() == Rol.SUPERADMINISTRADOR);

        if (!existeSuperAdmin) {
            adminService.registrarAdministrador(new AdministradorDTO(
                    1,
                    "Super Admin Default",
                    "Av. Corrientes 1000",
                    "1100000000",
                    "superadmin",   // Usuario para login
                    "super123",     // Contraseña para login
                    Rol.SUPERADMINISTRADOR
            ));
        }

        // Inicializar Administrador (Usando ID base 100)
        boolean existeAdmin = adminService.listarTodos().stream()
                .anyMatch(a -> a.getRol() == Rol.ADMINISTRADOR);

        if (!existeAdmin) {
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