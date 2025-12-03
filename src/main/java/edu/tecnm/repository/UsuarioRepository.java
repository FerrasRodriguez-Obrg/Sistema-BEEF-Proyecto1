// src/main/java/edu/tecnm/repository/UsuarioRepository.java

package edu.tecnm.repository;

import edu.tecnm.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    /**
     * Busca un usuario por su nombre de usuario (username).
     * Esto es fundamental para el proceso de autenticación de Spring Security.
     */
    Usuario findByUsername(String username);
}