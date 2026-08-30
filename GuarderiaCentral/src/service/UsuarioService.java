package service;

import model.Usuario;
import model.Rol;
import dto.UsuarioDTO;
import mapper.UsuarioMapper; // Usamos el mapper genérico
import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import exceptions.CredencialesInvalidasException;
import exceptions.RegistroNoEncontradoException;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     * Valida las credenciales. Retorna el modelo porque el sistema necesita
     * acceder a los métodos específicos de la clase Usuario/Hijas.
     */
    public Usuario validarLogin(String nombreUsuario, String clave) throws CredencialesInvalidasException {
        Usuario u = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (u == null || !u.getClave().equals(clave)) {
            throw new CredencialesInvalidasException("Error: Usuario o contraseña incorrectos.");
        }

        return u;
    }

    public boolean tieneRol(Usuario usuario, Rol rolRequerido) {
        return usuario.getRol() == rolRequerido;
    }

    /**
     * Busca un usuario y retorna su DTO usando el UsuarioMapper.
     */
    public UsuarioDTO buscarPorNombreUsuario(String nombreUsuario) throws RegistroNoEncontradoException {
        Usuario u = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (u == null) {
            throw new RegistroNoEncontradoException("No se encontró el usuario: " + nombreUsuario);
        }
        return UsuarioMapper.toDto(u);
    }

    /**
     * Busca un usuario por ID y retorna su DTO.
     */
    public UsuarioDTO buscarUsuarioPorId(int id) throws RegistroNoEncontradoException {
        Usuario u = usuarioDAO.buscarPorId(id);
        if (u == null) {
            throw new RegistroNoEncontradoException("No se encontró el usuario con ID: " + id);
        }
        return UsuarioMapper.toDto(u);
    }

    /**
     * Lista todos los usuarios transformándolos a DTOs para la vista.
     */
    public List<UsuarioDTO> listarTodos() {
        return usuarioDAO.listarTodos().stream()
                .map(UsuarioMapper::toDto) // Usamos el mapper para la conversión
                .collect(Collectors.toList());
    }

    public void actualizarUsuario(UsuarioDTO dto) throws RegistroNoEncontradoException {
        Usuario u = UsuarioMapper.toModel(dto);
        usuarioDAO.actualizar(u);
    }

    public void eliminarUsuario(int id) throws RegistroNoEncontradoException {
        if (usuarioDAO.buscarPorId(id) == null) {
            throw new RegistroNoEncontradoException("No se encontró el usuario con ID: " + id);
        }
        usuarioDAO.eliminar(id);
    }

}
