// src/main/java/edu/tecnm/repository/PerfilRepository.java

package edu.tecnm.repository;

import edu.tecnm.modelo.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    
    // No se requiere ningún método adicional por ahora.
}