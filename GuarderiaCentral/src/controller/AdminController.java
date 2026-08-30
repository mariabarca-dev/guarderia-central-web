package controller;

import service.*;
import dto.*;
import exceptions.*;
import java.util.ArrayList;
import mapper.*;
import model.Usuario;
import java.util.List;
import model.AsignacionEmpleadoZona;
import model.AsignacionVehiculoGarage;
import model.Empleado;
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

    // --- Socios ---
    public List<SocioDTO> listarTodosLosSocios() {
        return socioService.listarTodos();
    }

    public SocioDTO buscarSocioPorId(int id) {
        try {
            // Llamamos al servicio y retornamos el resultado
            return socioService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            // Si no se encuentra, retornamos null para que el menú sepa que no existe
            return null;
        }
    }

    // --- Empleados ---
    public List<EmpleadoDTO> listarTodosLosEmpleados() {
        return empleadoService.listarTodos();
    }

    // --- Vehículos ---
    public List<VehiculoDTO> listarTodosLosVehiculos() {
        return vehiculoService.listarTodos();
    }

    public VehiculoDTO buscarVehiculoPorId(int id) {
        try {
            return vehiculoService.buscarPorId(id);
        } catch (Exception e) {
            return null;
        }
    }

    // El servicio requiere Matrícula (String), el menú tiene ID (int). Hacemos la conversión aquí:
    public void eliminarVehiculo(int id) {
        try {
            VehiculoDTO v = vehiculoService.buscarPorId(id);
            if (v != null) {
                vehiculoService.eliminarVehiculo(v.getMatricula());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarVehiculo(VehiculoDTO dto) throws ErrorNegocio, MatriculaDuplicadaException {
        try {
            // Validar matrícula duplicada (excepto si es el mismo vehículo que estamos modificando)
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula()) && v.getId() != dto.getId()) {
                    throw new MatriculaDuplicadaException("La matrícula " + dto.getMatricula() + " ya está registrada.");
                }
            }

            // Actualizar vehículo
            vehiculoService.actualizarVehiculo(dto);

        } catch (MatriculaDuplicadaException e) {
            throw e; // Propagamos la excepción específica
        } catch (ErrorNegocio e) {
            throw e; // Propagamos otras reglas de negocio
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al modificar el vehículo: " + e.getMessage());
        }
    }

    // --- Garajes ---
    public List<GarageDTO> listarTodosLosGarages() {
        return garageService.listarTodos();
    }

    public List<String> listarDisponibilidadGarages() {
        return garageService.consultarDisponibilidadGarages();
    }

    // --- Zonas ---
    public List<ZonaDTO> listarTodasLasZonas() {
        return zonaService.listarTodas();
    }

    // El servicio usa 'letra' (String). El menú debe buscar por letra.
    public ZonaDTO buscarZonaPorLetra(String letra) {
        try {
            return zonaService.buscarPorLetra(letra);
        } catch (Exception e) {
            return null;
        }
    }

    public void modificarZona(ZonaDTO z) {
        try {
            zonaService.actualizarZona(z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // El servicio usa 'letra' (String) para eliminar.
    public void eliminarZonaPorLetra(String letra) {
        try {
            zonaService.eliminarZona(letra);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Asignaciones ---
    //  public AsignacionVehiculoGarageDTO buscarAsignacionPorGarage(int id) {
    //      return asignacionVehiculoGarageService.buscarPorGarage(id);
    //  }
    // --- Dentro de AdminController.java ---
// 🔹 Corrección del método para buscar asignación por ID de garaje
    public AsignacionVehiculoGarageDTO buscarAsignacionPorGarage(int id) {
        try {
            // 1. Primero, buscamos el objeto Garage completo usando el ID
            Garage garageCompleto = garageService.buscarPorId(id);

            // 2. Ahora, pasamos el objeto Garage completo al servicio
            AsignacionVehiculoGarage asignacion = asignacionVehiculoGarageService.buscarPorGarage(garageCompleto);

            // 3. Mapeamos el resultado a DTO y lo retornamos
            return AsignacionVehiculoGarageMapper.toDto(asignacion);

        } catch (RegistroNoEncontradoException e) {
            // Si no se encuentra el garaje o la asignación, retornamos null para el menú
            return null;
        } catch (Exception e) {
            // Manejo de otros errores técnicos
            e.printStackTrace();
            return null; // O podrías lanzar una excepción personalizada de controlador
        }
    }

    // --- Usuarios ---
    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioService.listarTodos();
    }

    public void modificarUsuario(UsuarioDTO u) {
        try {
            usuarioService.actualizarUsuario(u);
        } catch (Exception e) {
        }
    }

    public void eliminarUsuario(int id) {
        try {
            usuarioService.eliminarUsuario(id);
        } catch (Exception e) {
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

    public void registrarGaraje(GarageDTO dto) throws ErrorNegocio {
        try {
            garageService.registrarGarage(dto);
        } catch (ErrorNegocio e) {
            // Propagamos la excepción para que el menú la capture y muestre el mensaje
            throw e;
        } catch (Exception e) {
            // Cualquier otro error inesperado
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al registrar el garaje: " + e.getMessage());
        }
    }

    public void modificarGarage(GarageDTO garage) {
        try {
            garageService.actualizarGarage(garage);
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrarZona(ZonaDTO dto) throws ErrorNegocio {
        try {
            // Validar existencia antes de registrar
            try {
                ZonaDTO existente = zonaService.buscarPorLetra(String.valueOf(dto.getLetra()));
                if (existente != null) {
                    throw new ErrorNegocio("Ya existe una zona con la letra: " + dto.getLetra());
                }
            } catch (RegistroNoEncontradoException e) {
                // Si no se encuentra, podemos continuar
            }

            zonaService.registrarZona(dto);
        } catch (ErrorNegocio e) {
            throw e; // Propagamos para que el menú lo capture
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al registrar la zona: " + e.getMessage());
        }
    }

    public void registrarVehiculo(VehiculoDTO dto) throws ErrorNegocio {
        try {
            // Validar que la matrícula no esté duplicada
            List<VehiculoDTO> existentes = vehiculoService.listarTodos();
            for (VehiculoDTO v : existentes) {
                if (v.getMatricula().equalsIgnoreCase(dto.getMatricula())) {
                    throw new ErrorNegocio("Ya existe un vehículo con matrícula: " + dto.getMatricula());
                }
            }

            // Registrar vehículo
            vehiculoService.registrarVehiculo(dto);
        } catch (ErrorNegocio e) {
            throw e; // Propagamos para que el menú lo capture
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al registrar el vehículo: " + e.getMessage());
        }
    }

    //////////////////////////////////////////////////77
    ////EUGEEEE MMMMMETODOS
    /////////////////////////////////////////////////7
    
    
    // --- Dentro de AdminController.java ---

public GarageDTO buscarGaragePorId(int id) {
        try {
            // Llamamos al servicio, que nos devuelve el modelo
            Garage garage = garageService.buscarPorId(id);

            // Convertimos el modelo a DTO usando el Mapper
            return GarageMapper.toDto(garage);
        } catch (RegistroNoEncontradoException e) {
            // Si no existe, retornamos null para que el menú sepa qué hacer
            return null;
        }
    }

    public void asignarPropiedadGarage(PropiedadGarageDTO dto) throws ErrorNegocio {
        try {
            // Delegamos al servicio
            propiedadGarageService.registrarPropiedad(dto);
        } catch (ErrorNegocio e) {
            // Propagamos la excepción específica de negocio (la fecha inválida)
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al asignar la propiedad: " + e.getMessage());
        }
    }

    public void asignarVehiculoAGarageDTO(AsignacionVehiculoGarageDTO dto) throws ErrorNegocio {

        // Delegamos al servicio la lógica completa (validaciones, fechas, capacidad, etc.)
        asignacionVehiculoGarageService.crearAsignacion(dto);

    }

    public List<VehiculoDTO> listarVehiculosPorZona(int zonaId) {
        // 1. Obtenemos todos los vehículos
        List<VehiculoDTO> todosLosVehiculos = vehiculoService.listarTodos();
        List<VehiculoDTO> resultado = new ArrayList<>();

        // 2. Filtramos. Necesitamos saber si el vehículo está en un garaje de esa zona.
        // Usamos el servicio de asignaciones para verificar la ubicación.
        for (VehiculoDTO v : todosLosVehiculos) {
            // Necesitas un método en tu asignacionService que busque por vehículo
            var asignacion = asignacionVehiculoGarageService.buscarPorVehiculo(v.getId());

            if (asignacion != null && asignacion.getGarage().getZona().getId() == zonaId) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /////DANIIIIIII

public void asignarEmpleadoAZona(AsignacionEmpleadoZonaDTO dto) throws ErrorNegocio {
        try {
            // Validación: que la zona exista y tenga capacidad
            ZonaDTO zona = null;
            List<ZonaDTO> zonas = zonaService.listarTodas();
            for (ZonaDTO z : zonas) {
                if (z.getId() == dto.getZona().getId()) {
                    zona = z;
                    break;
                }
            }
            if (zona == null) {
                throw new ErrorNegocio("La zona no existe.");
            }

            // Validación: que el empleado exista
            EmpleadoDTO empleado = buscarEmpleadoPorId(dto.getEmpleado().getId());
            if (empleado == null) {
                throw new ErrorNegocio("El empleado no existe.");
            }

            // Delegamos al service
            asignacionEmpleadoZonaService.crearAsignacion(dto);

        } catch (ErrorNegocio e) {
            throw e; // Propagamos la excepción de negocio
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorNegocio("Error inesperado al asignar empleado a zona: " + e.getMessage());
        }
    }

    public ZonaDTO buscarZonaPorId(int idZona) {
        List<ZonaDTO> zonas = zonaService.listarTodas();
        for (ZonaDTO z : zonas) {
            if (z.getId() == idZona) {
                return z;
            }
        }
        return null; // No encontrado
    }

    public void listarEmpleadosPorZona(int idZona) {
        List<AsignacionEmpleadoZona> asignaciones = asignacionEmpleadoZonaService.listarTodas();

        System.out.println("Empleados asignados a la zona " + idZona + ":");
        for (AsignacionEmpleadoZona asg : asignaciones) {
            if (asg.getZona().getId() == idZona) {
                Empleado emp = asg.getEmpleado();
                System.out.println("ID: " + emp.getId()
                        + " | Código: " + emp.getCodigo()
                        + " | Nombre: " + emp.getNombre());
            }
        }
    }

    public EmpleadoDTO buscarEmpleadoPorId(int idEmpleado) {
        List<EmpleadoDTO> empleados = empleadoService.listarTodos();
        for (EmpleadoDTO e : empleados) {
            if (e.getId() == idEmpleado) {
                return e;
            }
        }
        return null; // No encontrado
    }

}
