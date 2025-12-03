package edu.tecnm.repository;

import edu.tecnm.modelo.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

// Extiende JpaRepository, usando la entidad Empleado y el tipo de dato de su ID (Integer)
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    
}