package controller;

import service.*;
import dto.*;
import exceptions.*;
import java.util.ArrayList;
import mapper.*;
import model.Usuario;
import java.util.List;
import model.AsignacionVehiculoGarage;
import model.Garage;

//prueba conexion fran

public class AdminController {

    private final SocioService socioService = new SocioService();
    private final EmpleadoService empleadoService = new EmpleadoService();
    private final AdministradorService administradorservice = new AdministradorService();
    private final VehiculoService vehiculoService = new VehiculoService();
    private final GarageService garageService = new GarageService();
    private final ZonaService zonaService = new ZonaService();
    private final AsignacionVehiculoGarageService asignacionVehiculoGarageService = new AsignacionVehiculoGarageService();
    private final AsignacionEmpleadoZonaService asignacionEmpleadoZonaService = new AsignacionEmpleadoZonaService();
    private final PropiedadGarageService propiedadGarageService = new PropiedadGarageService();
    private final UsuarioService usuarioService = new UsuarioService();

    public AdminController(Usuario usuario) {
    }

    public String registrarAdministradorDesdeVista(String nombre, String direccion, String telefono,
                                                   String nombreUsuario, String clave, String rolStr) {

        // 1. VALIDACIONES DE SINTAXIS Y FORMATO (Controller)
        if (nombre == null || nombre.isBlank() || nombreUsuario == null || nombreUsuario.isBlank()) {
            return "Error de sintaxis: El nombre y el usuario son obligatorios.";
        }

        if (telefono != null && !telefono.isBlank() && !telefono.matches("\\d+")) {
            return "Error de formato: El teléfono solo debe contener números.";
        }

        // 2. ARMADO DEL DTO Y LLAMADA AL SERVICE (Reglas de Negocio)
        try {
            AdministradorDTO dto = new AdministradorDTO();
            dto.setNombre(nombre.trim());
            dto.setDireccion(direccion);
            dto.setTelefono(telefono);
            dto.setNombreUsuario(nombreUsuario.trim());
            dto.setClave(clave);

            administradorservice.registrarAdministrador(dto);
            return "Éxito: Administrador registrado correctamente.";

        } catch (ErrorNegocio e) {
            return "Error de negocio: " + e.getMessage();
        }
    }

    // --- Socios ---
    public List<SocioDTO> listarTodosLosSocios() {
        return socioService.listarTodos();
    }

    public SocioDTO buscarSocioPorId(int id) {
        try {
            return socioService.buscarPorId(id);
        } catch (ErrorNegocio e) {
            return null;
        }
    }

    // --- Empleados ---
    public List<EmpleadoDTO> listarTodosLosEmpleados() {
        return empleadoService.listarTodos();
    }

    public EmpleadoDTO buscarEmpleadoPorId(int id) {
        return empleadoService.buscarEmpleadoPorId(id);
    }

    // --- Vehículos ---
    public List<VehiculoDTO> listarTodosLosVehiculos() {
        return vehiculoService.listarTodos();
    }

    public VehiculoDTO buscarVehiculoPorId(int id) {
        try {
            return vehiculoService.buscarPorId(id);
        } catch (ErrorNegocio e) {
            return null;
        }
    }

    public void eliminarVehiculo(int id) {
        try {
            VehiculoDTO v = vehiculoService.buscarPorId(id);
            if (v != null) {
                vehiculoService.eliminarVehiculo(v.getMatricula());
            }
        } catch (ErrorNegocio e) {
            System.out.println("Error al eliminar vehículo: " + e.getMessage());
        }
    }

    public void modificarVehiculo(VehiculoDTO dto) throws ErrorNegocio {
        try {
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula()) && v.getId() != dto.getId()) {
                    throw new ErrorNegocio("La matrícula " + dto.getMatricula() + " ya está registrada.");
                }
            }
            vehiculoService.actualizarVehiculo(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al modificar el vehículo: " + e.getMessage());
        }
    }

    public void registrarVehiculo(VehiculoDTO dto) throws ErrorNegocio {
        try {
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula())) {
                    throw new ErrorNegocio("Ya existe un vehículo con matrícula: " + dto.getMatricula());
                }
            }
            vehiculoService.registrarVehiculo(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al registrar el vehículo: " + e.getMessage());
        }
    }

    // --- Garajes ---
    public List<GarageDTO> listarTodosLosGarages() {
        return garageService.listarTodos();
    }

    public List<String> listarDisponibilidadGarages() {
        return garageService.consultarDisponibilidadGarages();
    }

    public GarageDTO buscarGaragePorId(int id) {
        try {
            Garage garage = garageService.buscarPorId(id);
            return GarageMapper.toDto(garage);
        } catch (ErrorNegocio e) {
            return null;
        }
    }

    public void registrarGaraje(GarageDTO dto) throws ErrorNegocio {
        try {
            garageService.registrarGarage(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al registrar el garaje: " + e.getMessage());
        }
    }

    public void modificarGarage(GarageDTO garage) {
        try {
            garageService.actualizarGarage(garage);
        } catch (ErrorNegocio e) {
            System.out.println("Error al modificar garaje: " + e.getMessage());
        }
    }

    public void eliminarGarage(int id) {
        try {
            GarageDTO garageEncontrado = null;
            for (GarageDTO g : garageService.listarTodos()) {
                if (g.getId() == id) {
                    garageEncontrado = g;
                    break;
                }
            }

            if (garageEncontrado != null) {
                garageService.eliminarGarage(garageEncontrado.getNumeroGarage());
            } else {
                System.out.println("No se encontró ningún garaje con ese ID.");
            }
        } catch (ErrorNegocio e) {
            System.out.println("Error al eliminar garaje: " + e.getMessage());
        }
    }

    // --- Zonas ---
    public List<ZonaDTO> listarTodasLasZonas() {
        return zonaService.listarTodas();
    }

    public ZonaDTO buscarZonaPorLetra(String letra) {
        try {
            return zonaService.buscarPorLetra(letra);
        } catch (ErrorNegocio e) {
            return null;
        }
    }

    public ZonaDTO buscarZonaPorId(int idZona) {
        List<ZonaDTO> zonas = zonaService.listarTodas();
        for (ZonaDTO z : zonas) {
            if (z.getId() == idZona) {
                return z;
            }
        }
        return null;
    }

    public void registrarZona(ZonaDTO dto) throws ErrorNegocio {
        try {
            try {
                ZonaDTO existente = zonaService.buscarPorLetra(String.valueOf(dto.getLetra()));
                if (existente != null) {
                    throw new ErrorNegocio("Ya existe una zona con la letra: " + dto.getLetra());
                }
            } catch (ErrorNegocio e) {
                // Si no se encuentra la zona, procedemos con el registro
            }

            zonaService.registrarZona(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al registrar la zona: " + e.getMessage());
        }
    }

    public void modificarZona(ZonaDTO z) {
        try {
            zonaService.actualizarZona(z);
        } catch (ErrorNegocio e) {
            System.out.println("Error al modificar zona: " + e.getMessage());
        }
    }

    public void eliminarZonaPorLetra(String letra) {
        try {
            zonaService.eliminarZona(letra);
        } catch (ErrorNegocio e) {
            System.out.println("Error al eliminar zona: " + e.getMessage());
        }
    }

    // --- Asignaciones y Propiedades ---
    public AsignacionVehiculoGarageDTO buscarAsignacionPorGarage(int id) {
        try {
            Garage garageCompleto = garageService.buscarPorId(id);
            AsignacionVehiculoGarage asignacion = asignacionVehiculoGarageService.buscarPorGarage(garageCompleto);
            return AsignacionVehiculoGarageMapper.toDto(asignacion);
        } catch (ErrorNegocio e) {
            return null;
        }
    }

    public void asignarPropiedadGarage(PropiedadGarageDTO dto) throws ErrorNegocio {
        try {
            propiedadGarageService.registrarPropiedad(dto);
        } catch (ErrorNegocio e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorNegocio("Error inesperado al asignar la propiedad: " + e.getMessage());
        }
    }

    public void asignarVehiculoAGarageDTO(AsignacionVehiculoGarageDTO dto) throws ErrorNegocio {
        try {
            asignacionVehiculoGarageService.crearAsignacion(dto);
        } catch (ErrorNegocio e) {
            throw e;
        }
    }

    public List<VehiculoDTO> listarVehiculosPorZona(int zonaId) {
        List<VehiculoDTO> todosLosVehiculos = vehiculoService.listarTodos();
        List<VehiculoDTO> resultado = new ArrayList<>();

        for (VehiculoDTO v : todosLosVehiculos) {
            var asignacion = asignacionVehiculoGarageService.buscarPorVehiculo(v.getId());
            if (asignacion != null && asignacion.getGarage().getZona().getId() == zonaId) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public void asignarEmpleadoAZona(AsignacionEmpleadoZonaDTO dto) throws ErrorNegocio {
        try {
            asignacionEmpleadoZonaService.asignarEmpleadoAZona(dto);
        } catch (ErrorNegocio e) {
            throw e;
        }
    }

    public List<EmpleadoDTO> listarEmpleadosPorZona(int zonaId) {
        return asignacionEmpleadoZonaService.obtenerEmpleadosPorZona(zonaId);
    }

    // --- Usuarios ---
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioService.listarTodos();
    }

    public void modificarUsuario(UsuarioDTO u) {
        try {
            usuarioService.actualizarUsuario(u);
        } catch (ErrorNegocio e) {
            System.out.println("Error al modificar usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(int id) {
        try {
            usuarioService.eliminarUsuario(id);
        } catch (ErrorNegocio e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    public void registrarUsuario(UsuarioDTO usuario) throws ErrorNegocio {
        if (usuario instanceof SocioDTO socioDTO) {
            socioService.registrarSocio(socioDTO);
        } else if (usuario instanceof EmpleadoDTO empleadoDTO) {
            empleadoService.registrarEmpleado(empleadoDTO);
        } else if (usuario instanceof AdministradorDTO administradorDTO) {
            administradorservice.registrarAdministrador(administradorDTO);
        } else {
            throw new ErrorNegocio("Tipo de usuario no soportado: " + usuario.getClass().getSimpleName());
        }
    }
}