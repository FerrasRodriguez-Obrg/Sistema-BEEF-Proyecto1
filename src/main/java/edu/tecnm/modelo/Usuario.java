// src/main/java/edu/tecnm/modelo/Usuario.java

package edu.tecnm.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="username", unique=true, nullable=false, length=50)
    private String username;

    @Column(nullable=false, length=60)
    private String password;

    private String nombre;
    private String email;
    private boolean estatus; // Indica si la cuenta está activa (true/false)
    private LocalDate fechaRegistro;

    // Relación Muchos a Muchos: Un Usuario tiene muchos Perfiles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USUARIO_PERFIL", 
               joinColumns = @JoinColumn(name = "idUsuario"), 
               inverseJoinColumns = @JoinColumn(name = "idPerfil"))
    private List<Perfil> perfiles;

    // **********************************
    // GETTERS y SETTERS
    // **********************************

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEstatus() {
        return estatus;
    }
    
    // Si usas Integer para estatus en BD, puedes cambiar boolean a Integer aquí y en los campos.
    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }
}