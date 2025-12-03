package edu.tecnm.service;

import edu.tecnm.modelo.Empleado;
import java.util.List;

public interface IEmpleadoService {
    
    // Métodos esenciales
    List<Empleado> buscarTodos();
    Empleado buscarPorId(Integer id);
    Empleado guardar(Empleado empleado);
    void eliminar(Integer id);
}