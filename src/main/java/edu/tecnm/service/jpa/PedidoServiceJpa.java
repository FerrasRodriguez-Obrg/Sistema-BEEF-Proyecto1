package edu.tecnm.service.jpa;

import edu.tecnm.modelo.Pedido;
import edu.tecnm.modelo.DetallePedido; 
import edu.tecnm.modelo.Reservacion; 
import edu.tecnm.repository.PedidoRepository;
import edu.tecnm.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional; 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate; // 🛑 Import necesario para el filtro

@Service("pedidoServiceJpa")
public class PedidoServiceJpa implements IPedidoService {

    @Autowired
    private PedidoRepository repo; 

    @Override
    public List<Pedido> buscarTodosPedido() {
        return repo.findAll();
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        return repo.save(pedido);        
    }

    // 🛑 MÉTODO CORREGIDO: Contiene la lógica de carga forzada para la vista
    @Override
    @Transactional
    public Pedido buscarPorId(Integer id) {
        Optional<Pedido> optional = repo.findById(id);
        
        if (optional.isPresent()) {
            Pedido pedido = optional.get();
            
            // 1. CARGA DE DETALLES DEL PEDIDO (Productos)
            if (pedido.getDetalles() != null) {
                pedido.getDetalles().size(); 
                
                for (DetallePedido detalle : pedido.getDetalles()) {
                    if (detalle.getProducto() != null) {
                        detalle.getProducto().getNombre(); // Forzar carga del Producto
                    }
                }
            }
            
            // 2. CARGA DE LA RESERVACIÓN Y SUS RELACIONES ANIDADAS
            if (pedido.getReservacionOrigen() != null) {
                 Reservacion reserva = pedido.getReservacionOrigen();
                 
                 if (reserva.getMesaAsignada() != null) {
                     reserva.getMesaAsignada().getIdMesa(); 
                 }
                 
                 if (reserva.getEmpleadoAsignado() != null) {
                     reserva.getEmpleadoAsignado().getNombre(); 
                 }
                 
                 if (reserva.getCliente() != null) {
                     reserva.getCliente().getNombre();
                 }
            }
            
            return pedido;
        }
        return null;
    }
    
    // 🛑 IMPLEMENTACIÓN AGREGADA: Buscar por Fecha
    @Override
    public List<Pedido> buscarPorFecha(LocalDate fecha) {
        return repo.findByFecha(fecha);
    }
    
    // 🛑 IMPLEMENTACIÓN AGREGADA: Buscar por Cliente
    @Override
    public List<Pedido> buscarPorCliente(Integer idCliente) {
        return repo.findByCliente_Id(idCliente);
    }

    // 🛑 IMPLEMENTACIÓN AGREGADA: Buscar por Fecha Y Cliente
    @Override
    public List<Pedido> buscarPorFechaYCliente(LocalDate fecha, Integer idCliente) {
        return repo.findByFechaAndCliente_Id(fecha, idCliente);
    }

    // 🛑 Implementación para buscar por ID de Reservación (Usando el stream como fallback, aunque un findByReservacionOrigen_Id en el repo sería mejor)
    @Override
    public Pedido buscarPorIdReservacion(Integer idReservacion) {
        // Asume que el Repositorio puede buscar por la FK de la reserva.
        return repo.findAll().stream()
                     .filter(p -> p.getReservacionOrigen() != null && p.getReservacionOrigen().getId().equals(idReservacion))
                     .findFirst().orElse(null);
    }
    
    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}