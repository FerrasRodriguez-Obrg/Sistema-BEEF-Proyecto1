package edu.tecnm.service.jpa;

import edu.tecnm.modelo.Mesa;
import edu.tecnm.repository.MesaRepository;
import edu.tecnm.service.IMesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("mesaServiceJpa")
public class MesaServiceJpa implements IMesaService {

    @Autowired
    private MesaRepository repo;

    @Override
    public List<Mesa> buscarTodas() {
        return repo.findAll();
    }

    @Override
    public Mesa buscarMesaPorId(Integer id) {
        Optional<Mesa> optional = repo.findById(id);
        return optional.orElse(null);
    }
    
    // MÉTODO AÑADIDO: Implementación de guardar(Mesa)
    @Override
    public Mesa guardar(Mesa mesa) {
        return repo.save(mesa);
    }
    
    // MÉTODO AÑADIDO: Implementación de eliminar(Integer)
    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}