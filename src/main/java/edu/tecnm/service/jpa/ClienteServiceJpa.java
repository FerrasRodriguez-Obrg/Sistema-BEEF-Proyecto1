package edu.tecnm.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import edu.tecnm.modelo.Cliente;
import edu.tecnm.repository.ClienteRepository;
import edu.tecnm.service.IClienteService;
import jakarta.transaction.Transactional;

@Service
@Primary
public class ClienteServiceJpa implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepo;

    // ---------------------- CRUD BÁSICO ----------------------
    @Override
    public List<Cliente> buscarTodosClientes() {
        return clienteRepo.findAll();
    }

    @Override
    public Cliente buscarPorIdCliente(Integer idCliente) {
        Optional<Cliente> optional = clienteRepo.findById(idCliente);
        return optional.orElse(null);
    }

    @Override
    @Transactional
    public void guardarCliente(Cliente cliente) {
        clienteRepo.save(cliente);
    }

    @Override
    @Transactional
    public void eliminarPorId(Integer idCliente) {
        if (clienteRepo.existsById(idCliente)) {
            clienteRepo.deleteById(idCliente);
        }
    }

    // ---------------------- BÚSQUEDAS HTML ----------------------

    @Override
    public Optional<Cliente> buscarPorNombreExacto(String nombre) {
        return clienteRepo.findFirstByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Cliente> buscarPorNombreExactoLista(String nombre) {
        return clienteRepo.findByNombreIgnoreCase(nombre);
    }

    @Override
    public List<Cliente> buscarPorNombreContiene(String q) {
        return clienteRepo.findByNombreContainingIgnoreCase(q);
    }

    @Override
    public Optional<Cliente> buscarPorEmailExacto(String email) {
        return clienteRepo.findByEmailIgnoreCase(email);
    }

    @Override
    public List<Cliente> buscarPorEmailTermina(String sufijo) {
        return clienteRepo.findByEmailEndingWithIgnoreCase(sufijo);
    }

    @Override
    public List<Cliente> buscarPorEmailContiene(String fragmento) {
        return clienteRepo.findByEmailContainingIgnoreCase(fragmento);
    }

    @Override
    public List<Cliente> buscarCreditoEntre(Double desde, Double hasta) {
        return clienteRepo.findByCreditoBetween(desde, hasta);
    }

    @Override
    public List<Cliente> buscarCreditoMayorIgual(Double minimo) {
        return clienteRepo.findByCreditoGreaterThanEqual(minimo);
    }

    @Override
    public List<Cliente> buscarDestacados() {
        // Si destacado es Integer (0 = no, >0 = sí)
        return clienteRepo.findByDestacadoGreaterThan(0);
        // Si fuera boolean, usar: return clienteRepo.findByDestacadoTrue();
    }

    @Override
    public List<Cliente> buscarNombreYCreditoMin(String nombre, Double minimo) {
        return clienteRepo.findByNombreContainingIgnoreCaseAndCreditoGreaterThanEqual(nombre, minimo);
    }

    @Override
    public List<Cliente> buscarFotoIgual(String file) {
        // Ajusta el nombre del campo de tu entidad (fotocliente o fotoCliente)
        return clienteRepo.findByFotocliente(file);
    }

    @Override
    public List<Cliente> buscarDestacadosConCreditoMin(Double minimo) {
        // Si destacado es Integer:
        return clienteRepo.findByDestacadoGreaterThanAndCreditoGreaterThanEqual(0, minimo);
        // Si fuera boolean: return clienteRepo.findByDestacadoTrueAndCreditoGreaterThanEqual(minimo);
    }

    @Override
    public List<Cliente> top5PorCredito() {
        return clienteRepo.findTop5ByOrderByCreditoDesc();
    }
    
    // ---------------------- BÚSQUEDA GENÉRICA (NUEVO) ----------------------
    
    @Override
    public List<Cliente> search(String keyword) {
        // Llama directamente al método JPQL definido en ClienteRepository
        return clienteRepo.search(keyword);
    }
}