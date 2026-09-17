package controller;

import dto.GarageDTO;
import service.GarageService;
import model.Usuario;
import model.Socio;
import model.Rol;
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import exceptions.ZonaSinCapacidadException;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GarageController {

    private final GarageService garageService;

    // Patrones de validación de sintaxis y formato
    private static final Pattern PATTERN_ZONA = Pattern.compile("^[a-zA-Z]{1,3}$");
    private static final Pattern PATTERN_DNI = Pattern.compile("^[0-9]{7,10}$");

    public GarageController() {
        this.garageService = new GarageService();
    }

    public void registrarGarage(Usuario usuarioSesion, GarageDTO dto) throws ZonaSinCapacidadException, ErrorNegocio, RegistroNoEncontradoException {
        validarRol(usuarioSesion, Rol.ADMINISTRADOR);

        if (dto == null) {
            throw new IllegalArgumentException("El objeto DTO no puede ser nulo.");
        }

        // Validaciones de formato y sintaxis
        if (dto.getNumeroGarage() <= 0) {
            throw new ErrorNegocio("Error de formato: El número de garaje debe ser un entero mayor a 0.");
        }

        if (dto.getLecturaLuz() < 0.0) {
            throw new ErrorNegocio("Error de formato: La lectura de luz no puede ser un valor negativo.");
        }

        if (dto.getZona() == null || !PATTERN_ZONA.matcher(dto.getZona().trim()).matches()) {
            throw new ErrorNegocio("Error de formato: La letra o código de la zona no es válido (debe tener entre 1 y 3 letras).");
        }

        if (dto.getSocioPropietario() != null && !dto.getSocioPropietario().trim().isEmpty() && !dto.getSocioPropietario().equalsIgnoreCase("Libre")) {
            String dni = dto.getSocioPropietario().trim();
            if (!PATTERN_DNI.matcher(dni).matches()) {
                throw new ErrorNegocio("Error de formato: El DNI del socio propietario no es válido (7 a 10 dígitos numéricos).");
            }
            dto.setSocioPropietario(dni);
        } else {
            dto.setSocioPropietario(null);
        }

        dto.setZona(dto.getZona().trim().toUpperCase());
        garageService.registrarGarage(dto);
    }

    public List<GarageDTO> listarGarajes(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);

        List<GarageDTO> todos = garageService.listarTodos();

        return switch (usuarioSesion.getRol()) {
            case ADMINISTRADOR, EMPLEADO -> todos;

            case SOCIO -> {
                if (usuarioSesion instanceof Socio socio) {
                    String dniSocio = socio.getDni();
                    yield todos.stream()
                            .filter(g -> g.getSocioPropietario() != null && g.getSocioPropietario().equals(dniSocio))
                            .collect(Collectors.toList());
                }
                yield List.of();
            }

            case SUPERADMINISTRADOR -> throw new SecurityException("Acceso denegado: El superadministrador únicamente gestiona usuarios.");
        };
    }

    public GarageDTO buscarPorNumero(Usuario usuarioSesion, int numeroGarage) throws RegistroNoEncontradoException {
        validarUsuarioAutenticado(usuarioSesion);

        if (numeroGarage <= 0) {
            throw new IllegalArgumentException("El número de garaje especificado no es válido.");
        }

        List<GarageDTO> todos = garageService.listarTodos();
        GarageDTO dto = todos.stream()
                .filter(g -> g.getNumeroGarage() == numeroGarage)
                .findFirst()
                .orElseThrow(() -> new RegistroNoEncontradoException("No se encontró el garaje número: " + numeroGarage));

        if (usuarioSesion.getRol() == Rol.SOCIO && usuarioSesion instanceof Socio socio) {
            if (dto.getSocioPropietario() == null || !dto.getSocioPropietario().equals(socio.getDni())) {
                throw new SecurityException("Acceso denegado: El garaje no está asociado a su cuenta de socio.");
            }
        }

        return dto;
    }

    public void actualizarGarage(Usuario usuarioSesion, GarageDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        validarRol(usuarioSesion, Rol.ADMINISTRADOR);

        if (dto == null || dto.getId() <= 0) {
            throw new IllegalArgumentException("El DTO debe incluir un ID de garaje válido.");
        }

        if (dto.getNumeroGarage() <= 0) {
            throw new ErrorNegocio("Error de formato: El número de garaje debe ser mayor a 0.");
        }

        if (dto.getLecturaLuz() < 0.0) {
            throw new ErrorNegocio("Error de formato: La lectura de luz no puede ser un valor negativo.");
        }

        if (dto.getZona() != null && !PATTERN_ZONA.matcher(dto.getZona().trim()).matches()) {
            throw new ErrorNegocio("Error de formato: La letra de la zona no es válida.");
        }

        if (dto.getSocioPropietario() != null && !dto.getSocioPropietario().trim().isEmpty() && !dto.getSocioPropietario().equalsIgnoreCase("Libre")) {
            String dni = dto.getSocioPropietario().trim();
            if (!PATTERN_DNI.matcher(dni).matches()) {
                throw new ErrorNegocio("Error de formato: El DNI del socio propietario no es válido.");
            }
            dto.setSocioPropietario(dni);
        } else {
            dto.setSocioPropietario(null);
        }

        if (dto.getZona() != null) {
            dto.setZona(dto.getZona().trim().toUpperCase());
        }

        garageService.actualizarGarage(dto);
    }

    public void eliminarGarage(Usuario usuarioSesion, int numeroGarage) throws RegistroNoEncontradoException {
        validarRol(usuarioSesion, Rol.ADMINISTRADOR);

        if (numeroGarage <= 0) {
            throw new IllegalArgumentException("El número de garaje especificado no es válido.");
        }

        garageService.eliminarGarage(numeroGarage);
    }

    public List<String> consultarDisponibilidadGarages(Usuario usuarioSesion) {
        validarUsuarioAutenticado(usuarioSesion);

        if (usuarioSesion.getRol() == Rol.SOCIO) {
            throw new SecurityException("Acceso denegado: Los socios no pueden consultar el reporte global de disponibilidad.");
        }

        return garageService.consultarDisponibilidadGarages();
    }

    private void validarUsuarioAutenticado(Usuario usuario) {
        if (usuario == null) {
            throw new SecurityException("Debe iniciar sesión para realizar esta operación.");
        }
    }

    private void validarRol(Usuario usuario, Rol rolRequerido) {
        validarUsuarioAutenticado(usuario);
        if (usuario.getRol() != rolRequerido) {
            throw new SecurityException("Acceso denegado: Se requieren permisos de " + rolRequerido + ".");
        }
    }
}