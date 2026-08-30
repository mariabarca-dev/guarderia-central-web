package dao;

import model.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer>{
    
    Usuario buscarPorNombreUsuario(String nombreUsuario);
    
}
