package edu.tecnm.service;

import java.util.List;
import java.util.Optional;

import edu.tecnm.modelo.Cliente;

public interface IClienteService {

    // ---------------------- CRUD BÁSICO ----------------------
    List<Cliente> buscarTodosClientes();

    Cliente buscarPorIdCliente(Integer idCliente);

    void guardarCliente(Cliente cliente);

    void eliminarPorId(Integer idCliente);

    // ---------------------- BÚSQUEDAS HTML ----------------------

    // 1) Nombre exacto
    Optional<Cliente> buscarPorNombreExacto(String nombre);
    List<Cliente> buscarPorNombreExactoLista(String nombre);

    // 2) Nombre contiene
    List<Cliente> buscarPorNombreContiene(String q);

    // 3) Email exacto
    Optional<Cliente> buscarPorEmailExacto(String email);

    // 4) Email termina / contiene
    List<Cliente> buscarPorEmailTermina(String sufijo);
    List<Cliente> buscarPorEmailContiene(String fragmento);

    // 5) Crédito entre
    List<Cliente> buscarCreditoEntre(Double desde, Double hasta);

    // 6) Crédito mayor o igual
    List<Cliente> buscarCreditoMayorIgual(Double minimo);

    // 7) Solo destacados
    List<Cliente> buscarDestacados();

    // 8) Nombre contiene & crédito >=
    List<Cliente> buscarNombreYCreditoMin(String nombre, Double minimo);

    // 9) Foto igual
    List<Cliente> buscarFotoIgual(String file);

    // 10) Destacados con crédito >=
    List<Cliente> buscarDestacadosConCreditoMin(Double minimo);

    // 11) Top 5 por crédito
    List<Cliente> top5PorCredito();
    
    // ---------------------- BÚSQUEDA GENÉRICA (NUEVO) ----------------------
    
    /**
     * Busca clientes por coincidencia en Nombre, Apellidos o Teléfono.
     * @param keyword Término de búsqueda.
     * @return Lista de clientes que coinciden.
     */
    List<Cliente> search(String keyword); 
}