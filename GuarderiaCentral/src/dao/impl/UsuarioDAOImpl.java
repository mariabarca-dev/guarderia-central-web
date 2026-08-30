package dao.impl;

import dao.*;
import database.ArchivoUsuario;
import model.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    
    private ArchivoUsuario bd;

    public UsuarioDAOImpl() {
        this.bd = new ArchivoUsuario();
        this.bd.inicializarBD(); // Se asegura de que SOLO su entorno esté listo
    }

    private final AdministradorDAO adminDAO = new AdministradorDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
    private final SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> todos = new ArrayList<>();
        
        todos.addAll(adminDAO.listarTodos());
        todos.addAll(empleadoDAO.listarTodos());
        todos.addAll(socioDAO.listarTodos());
        
        return todos;
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        Usuario u = adminDAO.buscarPorId(id);
        if (u != null) return u;
        
        u = empleadoDAO.buscarPorId(id);
        if (u != null) return u;
        
        return socioDAO.buscarPorId(id);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return listarTodos().stream()
                .filter(u -> u.getNombreUsuario().equals(nombreUsuario))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void actualizar(Usuario usuario) {
        if (usuario instanceof Administrador) {
            adminDAO.actualizar((Administrador) usuario);
        } else if (usuario instanceof Empleado) {
            empleadoDAO.actualizar((Empleado) usuario);
        } else if (usuario instanceof Socio) {
            socioDAO.actualizar((Socio) usuario);
        } else {
            throw new IllegalArgumentException("Tipo de usuario desconocido: " + usuario.getClass().getSimpleName());
        }
    }

    @Override
    public void eliminar(Integer id) {
        Usuario u = buscarPorId(id);
        if (u instanceof Administrador) {
            adminDAO.eliminar(id);
        } else if (u instanceof Empleado) {
            empleadoDAO.eliminar(id);
        } else if (u instanceof Socio) {
            socioDAO.eliminar(id);
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        // Delegamos al DAO correspondiente
        if (usuario instanceof Administrador) {
            adminDAO.guardar((Administrador) usuario);
        } else if (usuario instanceof Empleado) {
            empleadoDAO.guardar((Empleado) usuario);
        } else if (usuario instanceof Socio) {
            socioDAO.guardar((Socio) usuario);
        }
    }
}