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

    public void verificarConfiguracionInicial() {
        try {
            // 1. Inicializar SuperAdministrador
            boolean existeSuperAdmin = adminService.listarTodos().stream()
                    .anyMatch(a -> a.getRol() == Rol.SUPERADMINISTRADOR || "superadmin".equalsIgnoreCase(a.getNombreUsuario()));

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

            // 2. Inicializar Administrador
            boolean existeAdmin = adminService.listarTodos().stream()
                    .anyMatch(a -> (a.getRol() == Rol.ADMINISTRADOR) || "admin".equalsIgnoreCase(a.getNombreUsuario()));

            if (!existeAdmin) {
                adminService.registrarAdministrador(new AdministradorDTO(
                        100, "Admin Default", "Calle Falsa 123", "12345678", "admin", "admin123", Rol.ADMINISTRADOR
                ));
            }

            // 3. Inicializar Empleado
            boolean existeEmpleado = empleadoService.listarTodos().stream()
                    .anyMatch(e -> "EMP001".equalsIgnoreCase(e.getNombreUsuario()));

            if (empleadoService.listarTodos().isEmpty() && !existeEmpleado) {
                empleadoService.registrarEmpleado(new EmpleadoDTO(
                        300, "Empleado Default", "Av. Principal 456", "87654321", "EMP001", "empleado123", Rol.EMPLEADO, "EMP-01", "Cuidacoche"
                ));
            }

            // 4. Inicializar Socio
            boolean existeSocio = socioService.listarTodos().stream()
                    .anyMatch(s -> "socio".equalsIgnoreCase(s.getNombreUsuario()));

            if (socioService.listarTodos().isEmpty() && !existeSocio) {
                socioService.registrarSocio(new SocioDTO(
                        600, "Socio Default", "Calle Real 789", "11223344", "socio", "socio123", Rol.SOCIO, "46598674", LocalDate.now()
                ));
            }
        } catch (ErrorNegocio e) {
            // Manejo defensivo: Si ya existen los usuarios por defecto, no interrumpimos el arranque
            System.out.println("Aviso [InicializarDataBase]: Los usuarios iniciales ya están creados en el sistema.");
        }
    }
}