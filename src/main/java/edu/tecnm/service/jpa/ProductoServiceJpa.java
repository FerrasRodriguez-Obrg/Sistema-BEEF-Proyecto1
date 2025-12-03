package edu.tecnm.service.jpa; // Ajuste el paquete si es necesario

import edu.tecnm.modelo.Producto;
import edu.tecnm.repository.ProductoRepository; 
import edu.tecnm.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productoServiceJpa")
public class ProductoServiceJpa implements IProductoService {

    @Autowired
    private ProductoRepository repo; 

    @Override
    public List<Producto> buscarTodosProductos() {
        // CORRECTO: Utiliza findAll() para obtener la lista completa.
        return repo.findAll();
    }

    // CORRECTO: Utiliza IgnoreCase para asegurar que el filtro funcione
    // sin importar si se usa "Platillo" o "platillo".
    @Override
    public List<Producto> buscarPorTipo(String tipo) {
        return repo.findByTipoIgnoreCase(tipo); 
    }

    // ... (Otros métodos omitidos) ...
    @Override
    public Producto buscarPorIdProducto(Integer idProducto) {
        Optional<Producto> optional = repo.findById(idProducto);
        return optional.orElse(null);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return repo.save(producto);
    }

    @Override
    public void eliminarProducto(Integer id) {
        repo.deleteById(id);
    }
}