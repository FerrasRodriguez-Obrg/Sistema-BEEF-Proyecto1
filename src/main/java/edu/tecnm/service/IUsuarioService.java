package edu.tecnm.service;

import edu.tecnm.modelo.Usuario;
import java.util.List;

public interface IUsuarioService {

    /**
     * Guarda o actualiza un usuario, incluyendo la lógica para asignar perfiles.
     */
    Usuario guardarUsuario(Usuario usuario);
    
    /**
     * Busca un usuario por su nombre de usuario (username).
     */
    Usuario buscarPorUsername(String username);
    
    /**
     * 🛑 MÉTODO FALTANTE: Busca un usuario por su ID.
     */
    Usuario buscarPorId(Integer id); 
    
    List<Usuario> buscarTodos();
}