package controller;

import exceptions.*;
import service.*;
import dto.*;
import exceptions.RegistroNoEncontradoException;
import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class SocioController implements Controlador {

    private final SocioService socioService;
    private final VehiculoService vehiculoService;
    private final PropiedadGarageService propiedadGarageService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,100}$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern PATTERN_USUARIO = Pattern.compile("^[a-zA-Z0-9_.]{4,20}$");
    private static final Pattern PATTERN_CLAVE = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._]).{8,64}$");
    private static final Pattern PATTERN_DNI = Pattern.compile("^\\d{7,8}$");

    public SocioController(Usuario usuario) {
        if (usuario == null || (usuario.getRol() != Rol.SOCIO && usuario.getRol() != Rol.ADMINISTRADOR)) {
            throw new SecurityException("Acceso denegado: No tienes permisos para acceder a esta sección.");
        }
        this.socioService = new SocioService();
        this.vehiculoService = new VehiculoService();
        this.propiedadGarageService = new PropiedadGarageService();
    }

    public List<SocioDTO> listarTodosLosSocios() {
        return socioService.listarTodos();
    }

    public SocioDTO buscarSocioPorId(int id) {
        try {
            return socioService.buscarPorId(id);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public SocioDTO buscarSocioPorDni(String dni) {
        try {
            return socioService.buscarPorDni(dni);
        } catch (RegistroNoEncontradoException e) {
            return null;
        }
    }

    public void registrarSocio(SocioDTO dto) throws ErrorNegocio {
        // 1. Validaciones de sintaxis y formato en el controlador
        if (dto == null) {
            throw new ErrorNegocio("El socio no puede ser nulo.");
        }

        // Validación de DNI
        if (dto.getDni() == null || !PATTERN_DNI.matcher(dto.getDni()).matches()) {
            throw new ErrorNegocio("El formato del DNI no es válido (debe tener entre 7 y 8 dígitos numéricos).");
        }

        // Validación de Nombre
        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El nombre es obligatorio, debe tener entre 2 y 100 caracteres y contener solo letras y espacios.");
        }

        // Validación de Dirección
        if (dto.getDireccion() == null || dto.getDireccion().trim().isEmpty() || dto.getDireccion().length() > 200) {
            throw new ErrorNegocio("La dirección es obligatoria y no puede superar los 200 caracteres.");
        }

        // Validación de Teléfono
        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono()).matches()) {
            throw new ErrorNegocio("El formato del teléfono no es válido (7 a 20 caracteres permitiendo +, espacios y guiones).");
        }

        // Validación de Nombre de Usuario
        if (dto.getNombreUsuario() == null || !PATTERN_USUARIO.matcher(dto.getNombreUsuario()).matches()) {
            throw new ErrorNegocio("El nombre de usuario debe tener entre 4 y 20 caracteres (letras, números, puntos o guiones bajos).");
        }

        // Validación de Clave
        if (dto.getClave() == null || !PATTERN_CLAVE.matcher(dto.getClave()).matches()) {
            throw new ErrorNegocio("La clave no cumple con los requisitos de seguridad (mínimo 8 caracteres, incluir mayúscula, minúscula, número y carácter especial).");
        }

        // Validación de Rol
        if (dto.getRol() == null) {
            throw new ErrorNegocio("El rol del usuario es obligatorio.");
        }

        // Validación de Fecha de Ingreso (Sintaxis / Formato temporal básico)
        if (dto.getFechaIngreso() == null) {
            throw new ErrorNegocio("La fecha de ingreso es obligatoria.");
        }
        if (dto.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ErrorNegocio("La fecha de ingreso no puede estar en el futuro.");
        }

        // 2. Delegación directa al servicio para reglas de negocio
        socioService.registrarSocio(dto);
    }

    public void modificarSocio(SocioDTO dto) throws ErrorNegocio {
        if (dto == null || dto.getId() <= 0) {
            throw new ErrorNegocio("El ID del socio no es válido para la modificación.");
        }

        if (dto.getDni() == null || !PATTERN_DNI.matcher(dto.getDni()).matches()) {
            throw new ErrorNegocio("El formato del DNI es incorrecto.");
        }

        if (dto.getNombre() == null || !PATTERN_NOMBRE.matcher(dto.getNombre().trim()).matches()) {
            throw new ErrorNegocio("El formato del nombre no es válido.");
        }

        if (dto.getTelefono() == null || !PATTERN_TELEFONO.matcher(dto.getTelefono()).matches()) {
            throw new ErrorNegocio("El formato del teléfono no es válido.");
        }

        if (dto.getFechaIngreso() != null && dto.getFechaIngreso().isAfter(LocalDate.now())) {
            throw new ErrorNegocio("La fecha de ingreso no puede estar en el futuro.");
        }

        socioService.actualizarSocio(dto);
    }

    public void eliminarSocio(int id) throws ErrorNegocio {
        if (id <= 0) {
            throw new ErrorNegocio("El ID ingresado debe ser un número positivo.");
        }
        socioService.eliminarSocio(id);
    }

    public List<VehiculoDTO> consultarMisVehiculos(int socioId) {
        return vehiculoService.listarPorSocio(socioId);
    }

    public void consultarMiGarage(int socioId) {
        String reporte = propiedadGarageService.obtenerEstadoGarageSocio(socioId);
        System.out.println("--- Estado de mi Garage Propio ---");
        System.out.println(reporte);
        System.out.println("----------------------------------");
    }

    public List<VehiculoDTO> listarVehiculosPorSocio(int socioId) {
        return vehiculoService.listarPorSocio(socioId);
    }

    public List<GarageDTO> listarGarajesPorSocio(int socioId) {
        return propiedadGarageService.listarPorSocio(socioId);
    }

    @Override
    public void login(String nombreUsuario, String claveIngresada) {
    }
}