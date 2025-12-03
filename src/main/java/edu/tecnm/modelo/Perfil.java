// src/main/java/edu/tecnm/modelo/Perfil.java

package edu.tecnm.modelo;

import jakarta.persistence.*;

@Entity
// 🛑 CORRECCIÓN: Nombre de la tabla en minúscula
@Table(name = "perfiles") 
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String perfil; // Ej: ADMIN, EMPLEADO

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    @Override
    public String toString() {
        return "Perfil [id=" + id + ", perfil=" + perfil + "]";
    }
}