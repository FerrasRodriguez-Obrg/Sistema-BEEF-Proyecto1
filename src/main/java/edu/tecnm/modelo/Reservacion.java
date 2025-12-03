package edu.tecnm.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "reservacion")
public class Reservacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // --- Datos de la Cita ---
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private Integer numPersonas; // Cantidad de personas para la reserva
    
    // --- Estado de la Reserva (Clave para tu lógica) ---
    // Usamos String para los estados: "AUSENTE", "CONFIRMADA", "CANCELADA"
    private String estado; 

    // --- Relaciones Muchos a Uno (Foreign Keys) ---
    
    // Cliente: Quién hizo la reserva
    @ManyToOne(fetch = FetchType.EAGER) // Queremos ver el nombre del cliente inmediatamente
    @JoinColumn(name = "idCliente")
    private Cliente cliente; 
    
    // Mesa: El recurso asignado (relación mapeada en Mesa.java como "mesaAsignada")
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "idMesa")
    private Mesa mesaAsignada; // Nombre del campo en Mesa.java es "mesaAsignada"
    
    // Empleado: Quién está atendiendo esta reserva (relación mapeada en Empleado.java como "empleadoAsignado")
    @ManyToOne(fetch = FetchType.LAZY) // Asignación del empleado se puede cargar más tarde
    @JoinColumn(name = "idEmpleado")
    private Empleado empleadoAsignado; // Nombre del campo en Empleado.java es "empleadoAsignado"
    
    // Pedido: Relación Uno a Uno Opcional (el pedido se crea SOLO si la reserva se CONFIRMA)
    // Mantenemos la relación Uno a Uno si la lógica del negocio lo exige.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idPedidoAsociado") 
    private Pedido pedidoAsociado;

    // Constructor, Getters y Setters...
    
    public Reservacion() {}

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(LocalTime horaCita) {
        this.horaCita = horaCita;
    }

    public Integer getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(Integer numPersonas) {
        this.numPersonas = numPersonas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mesa getMesaAsignada() {
        return mesaAsignada;
    }

    public void setMesaAsignada(Mesa mesaAsignada) {
        this.mesaAsignada = mesaAsignada;
    }

    public Empleado getEmpleadoAsignado() {
        return empleadoAsignado;
    }

    public void setEmpleadoAsignado(Empleado empleadoAsignado) {
        this.empleadoAsignado = empleadoAsignado;
    }

    public Pedido getPedidoAsociado() {
        return pedidoAsociado;
    }

    public void setPedidoAsociado(Pedido pedidoAsociado) {
        this.pedidoAsociado = pedidoAsociado;
    }
}