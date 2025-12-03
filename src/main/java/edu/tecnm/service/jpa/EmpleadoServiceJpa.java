package edu.tecnm.service.jpa;

import edu.tecnm.modelo.Empleado;
import edu.tecnm.repository.EmpleadoRepository;
import edu.tecnm.service.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Necesario para buscar por ID

@Service("empleadoServiceJpa")
public class EmpleadoServiceJpa implements IEmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    // ----------------------------------------------------
    // 1. buscarTodos() (Este ya lo tenías)
    // ----------------------------------------------------
    @Override
    public List<Empleado> buscarTodos() {
        return repo.findAll();
    }
    
    // ----------------------------------------------------
    // 2. guardar(Empleado) <-- IMPLEMENTADO
    // ----------------------------------------------------
    @Override
    public Empleado guardar(Empleado empleado) {
        return repo.save(empleado);
    }
    
    // ----------------------------------------------------
    // 3. eliminar(Integer) <-- IMPLEMENTADO
    // ----------------------------------------------------
    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    // ----------------------------------------------------
    // 4. buscarPorId(Integer) <-- IMPLEMENTADO
    // ----------------------------------------------------
    @Override
    public Empleado buscarPorId(Integer id) {
        // Usa el método del repositorio que devuelve un Optional
        Optional<Empleado> optional = repo.findById(id);
        
        // Retorna la entidad si está presente, o null si no se encuentra
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}