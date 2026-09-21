package service;

import model.Socio;
import dto.SocioDTO;
import dto.VehiculoDTO;
import dto.GarageDTO;
import mapper.SocioMapper;
import dao.SocioDAO;
import dao.impl.SocioDAOImpl;
import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import exceptions.ErrorNegocio;
import exceptions.DniDuplicadoException;
import exceptions.RegistroNoEncontradoException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import util.IdGenerator;

public class SocioService {

    private final SocioDAO socioDAO;
    private final UsuarioDAO usuarioDAO;
    private final VehiculoService vehiculoService;
    private final PropiedadGarageService propiedadGarageService;

    public SocioService() {
        this.socioDAO = new SocioDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
        this.vehiculoService = new VehiculoService();
        this.propiedadGarageService = new PropiedadGarageService();
    }

    public void registrarSocio(SocioDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El socio (datos) no puede ser nulo.");
        }

        if (socioDAO.buscarPorDni(dto.getDni()) != null) {
            throw new DniDuplicadoException("Error de negocio: Ya existe un socio registrado con el DNI: " + dto.getDni());
        }

        if (usuarioDAO.buscarPorNombreUsuario(dto.getNombreUsuario()) != null) {
            throw new ErrorNegocio("Error de negocio: El nombre de usuario '" + dto.getNombreUsuario() + "' ya se encuentra en uso.");
        }

        LocalDate fechaFundacion = LocalDate.of(2000, 1, 1);
        if (dto.getFechaIngreso().isBefore(fechaFundacion)) {
            throw new ErrorNegocio("Error de negocio: La fecha de ingreso no puede ser anterior a la fecha de fundación del sistema (" + fechaFundacion + ").");
        }

        if (dto.getRol() != model.Rol.SOCIO && dto.getRol() != model.Rol.ADMINISTRADOR) {
            throw new ErrorNegocio("Error de negocio: El rol asignado no cuenta con los permisos permitidos para este tipo de registro.");
        }

        int nuevoId = IdGenerator.obtenerNuevoId("socio", 600);
        dto.setId(nuevoId);

        Socio socio = SocioMapper.toModel(dto);
        socioDAO.guardar(socio);
    }

    public SocioDTO buscarPorId(int id) throws RegistroNoEncontradoException {
        Socio s = socioDAO.buscarPorId(id);
        if (s == null) {
            throw new RegistroNoEncontradoException("No se encontró el socio con ID: " + id);
        }
        return SocioMapper.toDto(s);
    }

    public SocioDTO buscarPorDni(String dni) throws RegistroNoEncontradoException {
        Socio s = socioDAO.buscarPorDni(dni);
        if (s == null) {
            throw new RegistroNoEncontradoException("No se encontró el socio con DNI: " + dni);
        }
        return SocioMapper.toDto(s);
    }

    public List<SocioDTO> listarTodos() {
        return socioDAO.listarTodos().stream()
                .map(SocioMapper::toDto)
                .collect(Collectors.toList());
    }

    public void actualizarSocio(SocioDTO dto) throws RegistroNoEncontradoException, ErrorNegocio {
        Socio socioExistente = socioDAO.buscarPorId(dto.getId());
        if (socioExistente == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: Socio no encontrado con ID " + dto.getId());
        }

        Socio socioConMismoDni = socioDAO.buscarPorDni(dto.getDni());
        if (socioConMismoDni != null && socioConMismoDni.getId() != dto.getId()) {
            throw new ErrorNegocio("Error de negocio: El DNI " + dto.getDni() + " ya está asignado a otro socio.");
        }

        Socio socioActualizado = SocioMapper.toModel(dto);
        socioDAO.actualizar(socioActualizado);
    }

    public void eliminarSocio(int id) throws RegistroNoEncontradoException {
        if (socioDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: Socio no encontrado con ID " + id);
        }
        socioDAO.eliminar(id);
    }

    // Métodos delegados para resolver las consultas requeridas por el controlador
    public List<VehiculoDTO> listarVehiculosPorSocio(int socioId) {
        return vehiculoService.listarPorSocio(socioId);
    }

    public List<GarageDTO> listarGarajesPorSocio(int socioId) {
        return propiedadGarageService.listarPorSocio(socioId);
    }

    public String obtenerEstadoGarageSocio(int socioId) {
        return propiedadGarageService.obtenerEstadoGarageSocio(socioId);
    }
}