package edu.tecnm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar Query

import edu.tecnm.modelo.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // ==========================================================
    // 1) Buscar por nombre exacto (resultado único o lista)
    // ==========================================================
    Optional<Cliente> findFirstByNombreIgnoreCase(String nombre);
    List<Cliente> findByNombreIgnoreCase(String nombre);

    // ==========================================================
    // 2) Buscar por nombre que contiene letras
    // Corresponde a buscarPorNombreContiene(String q)
    // ==========================================================
    List<Cliente> findByNombreContainingIgnoreCase(String q);

    // ==========================================================
    // 3) Buscar por email exacto
    // ==========================================================
    Optional<Cliente> findByEmailIgnoreCase(String email);

    // ==========================================================
    // 4) Buscar por email que termina o contiene
    // Corresponde a buscarPorEmailTermina(String sufijo) y buscarPorEmailContiene(String fragmento)
    // ==========================================================
    List<Cliente> findByEmailEndingWithIgnoreCase(String suffix);
    List<Cliente> findByEmailContainingIgnoreCase(String fragment);

    // ==========================================================
    // 5) Buscar por rango de crédito (Corresponde a buscarCreditoEntre)
    // ==========================================================
    List<Cliente> findByCreditoBetween(Double desde, Double hasta);

    // ==========================================================
    // 6) Buscar crédito mayor o igual a un valor (Corresponde a buscarCreditoMayorIgual)
    // ==========================================================
    List<Cliente> findByCreditoGreaterThanEqual(Double minimo);

    // ==========================================================
    // 7) Buscar solo destacados (Corresponde a buscarDestacados)
    // ==========================================================
    List<Cliente> findByDestacadoGreaterThan(Integer valor);
    // Si usaras boolean en la entidad: List<Cliente> findByDestacadoTrue();

    // ==========================================================
    // 8) Buscar por nombre que contiene y crédito >= (Corresponde a buscarNombreYCreditoMin)
    // ==========================================================
    List<Cliente> findByNombreContainingIgnoreCaseAndCreditoGreaterThanEqual(String nombre, Double minimo);

    // ==========================================================
    // 9) Buscar por foto igual (Corresponde a buscarFotoIgual)
    // ==========================================================
    List<Cliente> findByFotocliente(String file);

    // ==========================================================
    // 10) Buscar destacados con crédito >= (Corresponde a buscarDestacadosConCreditoMin)
    // ==========================================================
    List<Cliente> findByDestacadoGreaterThanAndCreditoGreaterThanEqual(Integer valor, Double minimo);
    // Si usaras boolean en la entidad: List<Cliente> findByDestacadoTrueAndCreditoGreaterThanEqual(Double minimo);

    // ==========================================================
    // 11) Top 5 clientes con mayor crédito (Corresponde a top5PorCredito)
    // ==========================================================
    List<Cliente> findTop5ByOrderByCreditoDesc();
    
    // ==========================================================
    // 12) Búsqueda genérica por Nombre, Apellidos o Teléfono (NUEVO)
    // ==========================================================
    @Query("SELECT c FROM Cliente c WHERE "
            + "c.nombre LIKE %?1%"
            + " OR c.apellidos LIKE %?1%"
            + " OR c.telefono LIKE %?1%")
    List<Cliente> search(String keyword);
}