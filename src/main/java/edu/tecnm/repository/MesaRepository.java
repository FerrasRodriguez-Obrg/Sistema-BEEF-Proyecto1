package edu.tecnm.repository;

import edu.tecnm.modelo.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

// Extiende JpaRepository, usando la entidad Mesa y el tipo de dato de su ID (Integer)
public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    
}