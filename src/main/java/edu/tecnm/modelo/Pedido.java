package edu.tecnm.modelo;

import java.time.LocalDate;
import java.util.List; // Importar List
import jakarta.persistence.*;

@Entity
@Table(name = "pedido") // Indica que esta entidad mapea a la tabla 'pedido'
public class Pedido {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    private LocalDate fecha;
    private Double total;

    // Relación Muchos a Uno con Cliente (FK: idCliente)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCliente") 
    private Cliente cliente;

    // AÑADIDO: Relación Uno a Muchos con DetallePedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles;

    // 🛑 ADICIÓN CLAVE: Relación Uno a Uno Inversa con Reservacion
    // Mapeado por el campo "pedidoAsociado" en la clase Reservacion.java
    @OneToOne(mappedBy = "pedidoAsociado", fetch = FetchType.LAZY)
    private Reservacion reservacionOrigen;


    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    
    // 🛑 Getter y Setter para la Reservación
    public Reservacion getReservacionOrigen() {
        return reservacionOrigen;
    }

    public void setReservacionOrigen(Reservacion reservacionOrigen) {
        this.reservacionOrigen = reservacionOrigen;
    }

    @Override
    public String toString() {
        return "Pedido [id=" + id + ", fecha=" + fecha + ", total=" + total +
               ", cliente=" + (cliente != null ? cliente.getId() : null) + 
               ", detalles=" + (detalles != null ? detalles.size() : 0) + 
               ", reservacion=" + (reservacionOrigen != null ? reservacionOrigen.getId() : null) + "]";
    }
}