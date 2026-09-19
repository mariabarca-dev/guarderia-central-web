package service;

import model.Socio;
import dto.SocioDTO;
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

/**
 * Servicio para gestionar la lógica de negocio de los Socios.
 */
public class SocioService {

    private final SocioDAO socioDAO;
    private final UsuarioDAO usuarioDAO;

    public SocioService() {
        this.socioDAO = new SocioDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     * Registra un nuevo socio aplicando reglas de negocio estrictas.
     */
    public void registrarSocio(SocioDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("El socio (datos) no puede ser nulo.");
        }

        // 1. Regla de Negocio: Validar unicidad de DNI
        if (socioDAO.buscarPorDni(dto.getDni()) != null) {
            throw new DniDuplicadoException("Error de negocio: Ya existe un socio registrado con el DNI: " + dto.getDni());
        }

        // 2. Regla de Negocio: Validar unicidad de Nombre de Usuario
        if (usuarioDAO.buscarPorNombreUsuario(dto.getNombreUsuario()) != null) {
            throw new ErrorNegocio("Error de negocio: El nombre de usuario '" + dto.getNombreUsuario() + "' ya se encuentra en uso.");
        }

        // 3. Regla de Negocio: Restricción de antigüedad para fecha de ingreso
        // Por ejemplo, la fecha de ingreso no puede ser anterior a la fecha de fundación del club (ej. 1 de Enero de 2000)
        LocalDate fechaFundacion = LocalDate.of(2000, 1, 1);
        if (dto.getFechaIngreso().isBefore(fechaFundacion)) {
            throw new ErrorNegocio("Error de negocio: La fecha de ingreso no puede ser anterior a la fecha de fundación del sistema (" + fechaFundacion + ").");
        }

        // 4. Regla de Negocio: Consistencia de Rol (solo se permite registrar roles válidos para socios)
        if (dto.getRol() != model.Rol.SOCIO && dto.getRol() != model.Rol.ADMINISTRADOR) {
            throw new ErrorNegocio("Error de negocio: El rol asignado no cuenta con los permisos permitidos para este tipo de registro.");
        }

        // 5. Simulación de Cifrado de Clave / Hashing de Password por seguridad de negocio
        // dto.setClave(passwordEncoder.encode(dto.getClave()));

        // 6. Generación de ID para la nueva entidad
        int nuevoId = IdGenerator.obtenerNuevoId("socio", 600);
        dto.setId(nuevoId);

        // 7. Convertir DTO a Modelo y Persistir
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

        // Regla de negocio: Si se modifica el DNI, verificar que no pertenezca a otro socio diferente
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
}