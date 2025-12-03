package edu.tecnm.repository; 

import edu.tecnm.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    // Este método es heredado de JpaRepository y trae todos los productos.
    // List<Producto> findAll(); // No es necesario declararlo, pero existe.

    // Dejamos el findByTipoIgnoreCase por si lo usas en el filtro de admin
    List<Producto> findByTipoIgnoreCase(String tipo); 
}