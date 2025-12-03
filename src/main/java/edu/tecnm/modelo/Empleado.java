package edu.tecnm.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String puesto;
    private String telefono;
    private Boolean activo;
    private LocalDate fechaIngreso;

    // Relación INVERSA con Reservacion (Un empleado puede tener muchas reservaciones asignadas)
    @OneToMany(mappedBy = "empleadoAsignado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservacion> reservacionesAsignadas;

    // Constructores, Getters y Setters...
    
    public Empleado() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public List<Reservacion> getReservacionesAsignadas() {
        return reservacionesAsignadas;
    }

    public void setReservacionesAsignadas(List<Reservacion> reservacionesAsignadas) {
        this.reservacionesAsignadas = reservacionesAsignadas;
    }
}