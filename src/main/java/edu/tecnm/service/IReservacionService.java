package edu.tecnm.service;

import edu.tecnm.modelo.Reservacion;
import java.util.List;
import java.time.LocalDate; 

public interface IReservacionService {
	
    Reservacion buscarPorId(Integer id);

    List<Reservacion> buscarTodas();

    Reservacion guardar(Reservacion reservacion);

    void eliminar(Integer id);
    
    List<Reservacion> buscarPorFiltros(String clienteNombre, String estado, LocalDate fechaCita);
    
    /**
     * 🛑 NUEVO: Busca todas las reservaciones a partir de una fecha mínima (incluida).
     */
    List<Reservacion> buscarReservacionesDesdeHoy(LocalDate fechaMinima);
}