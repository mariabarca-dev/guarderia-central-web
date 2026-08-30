package service;

import model.Administrador;
import dto.AdministradorDTO;
import dao.AdministradorDAO;
import dao.impl.AdministradorDAOImpl; // Importación necesaria
import exceptions.ErrorNegocio;
import exceptions.RegistroNoEncontradoException;
import mapper.AdministradorMapper;
import util.IdGenerator;
import java.util.List;
import java.util.stream.Collectors; // Import necesario para el stream

public class AdministradorService {

    private AdministradorDAO administradorDAO;

    public AdministradorService() {
        // Inicialización directa de la implementación (acoplado, pero funcional para tu arquitectura actual)
        this.administradorDAO = new AdministradorDAOImpl();
    }

    // =================================================================================
    // GESTIÓN DE DATOS Y LISTADOS (Uso para InicializarDataBase y Vistas/Menús)
    // =================================================================================

    /**
     * Retorna una lista de todos los administradores convertidos a DTO.
     * Utilizado por la Vista/Menú para listar y por InicializarDataBase para verificar registros.
     */
    public List<AdministradorDTO> listarTodos() {
        // CORREGIDO: Se usa el stream y mapper para devolver List<AdministradorDTO>
        // en lugar de List<Administrador> (Modelo).
        return administradorDAO.listarTodos().stream()
                .map(AdministradorMapper::toDto)
                .collect(Collectors.toList());
    }

    // =================================================================================
    // OPERACIONES DE NEGOCIO (Llamadas desde Menú/Controlador vía DTO)
    // =================================================================================

    /**
     * Registra un nuevo administrador.
     * Validaciones de negocio: verifica unicidad de nombre de usuario.
     */
    public void registrarAdministrador(AdministradorDTO dto) throws ErrorNegocio {
        if (dto == null) {
            throw new ErrorNegocio("Los datos del administrador no pueden ser nulos.");
        }

        // 1. Validación de Regla de Negocio
        if (administradorDAO.existeAdministrador(dto.getNombreUsuario())) {
               throw new ErrorNegocio("El nombre de usuario ya existe.");
        }

        // 2. Generación del ID (Base 100 para administradores)
        int nuevoId = IdGenerator.obtenerNuevoId("administrador", 100);

        // 3. Conversión usando el Mapper (inyectando el ID generado)
        Administrador admin = AdministradorMapper.toModel(dto, nuevoId);

        // 4. Persistencia
        administradorDAO.guardar(admin);
    }

    /**
     * Busca un administrador por ID y lo retorna como DTO para la vista.
     */
    public AdministradorDTO buscarPorId(int id) throws RegistroNoEncontradoException {
        Administrador admin = administradorDAO.buscarPorId(id);
        if (admin == null) {
            throw new RegistroNoEncontradoException("No se encontró el administrador con ID: " + id);
        }
        // Convertimos a DTO antes de devolverlo
        return AdministradorMapper.toDto(admin);
    }

    /**
     * Elimina un administrador validando su existencia previa.
     */
    public void eliminarAdministrador(int id) throws RegistroNoEncontradoException {
        if (administradorDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se puede eliminar: El administrador con ID " + id + " no existe.");
        }
        administradorDAO.eliminar(id);
    }

    /**
     * Actualiza los datos de un administrador validando su existencia.
     */
    public void actualizarAdministrador(AdministradorDTO dto) throws ErrorNegocio, RegistroNoEncontradoException {
        if (dto == null) {
            throw new ErrorNegocio("Los datos a actualizar no pueden ser nulos.");
        }

        // Verificamos que exista antes de intentar actualizar
        if (administradorDAO.buscarPorId(dto.getId()) == null) {
            throw new RegistroNoEncontradoException("No se puede actualizar: El administrador con ID " + dto.getId() + " no existe.");
        }

        // Para actualizar, convertimos el DTO (que debe traer el ID correcto) a Modelo
        Administrador admin = AdministradorMapper.toModel(dto, dto.getId());

        administradorDAO.actualizar(admin);
    }
}