package edu.tecnm.repository;

import java.util.List;
import java.time.LocalDate; // Importamos LocalDate
import org.springframework.data.jpa.repository.JpaRepository;

import edu.tecnm.modelo.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    // Método existente para ordenar por fecha descendente
    List<Pedido> findAllByOrderByFechaDesc();

    // ******************************************************
    // 🏆 MÉTODOS AGREGADOS PARA EL FILTRO DE BÚSQUEDA
    // ******************************************************
    
    // 1. Buscar por Fecha
    // Spring Data genera: WHERE fecha = ?
    List<Pedido> findByFecha(LocalDate fecha);
    
    // 2. Buscar por Cliente (usa la FK idCliente dentro del objeto Cliente)
    // Spring Data genera: WHERE cliente.id = ?
    List<Pedido> findByCliente_Id(Integer idCliente);
    
    // 3. Buscar por Fecha Y Cliente
    // Spring Data genera: WHERE fecha = ? AND cliente.id = ?
    List<Pedido> findByFechaAndCliente_Id(LocalDate fecha, Integer idCliente);
}