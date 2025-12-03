package edu.tecnm.repository;

import edu.tecnm.modelo.Reservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservacionRepository extends JpaRepository<Reservacion, Integer> {
    
    /**
     * Búsqueda por filtros opcionales (Cliente, Estado, Fecha).
     */
    @Query("SELECT r FROM Reservacion r " +
           "WHERE (:clienteNombre IS NULL OR " +
           "       CONCAT(r.cliente.nombre, ' ', r.cliente.apellidos) LIKE %:clienteNombre%) " +
           "AND (:estado IS NULL OR r.estado = :estado) " +
           "AND (:fechaCita IS NULL OR r.fechaCita = :fechaCita)")
    List<Reservacion> findByFiltros(
        @Param("clienteNombre") String clienteNombre, 
        @Param("estado") String estado, 
        @Param("fechaCita") LocalDate fechaCita
    );
    
    /**
     * 🛑 NUEVO: Consulta que trae las reservaciones a partir de la fecha actual (o futura).
     */
    @Query("SELECT r FROM Reservacion r WHERE r.fechaCita >= :fechaMinima ORDER BY r.fechaCita ASC, r.horaCita ASC")
    List<Reservacion> findByFechaCitaGreaterThanEqual(@Param("fechaMinima") LocalDate fechaMinima);
}