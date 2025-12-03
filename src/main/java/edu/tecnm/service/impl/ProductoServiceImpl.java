package edu.tecnm.service.impl; // 🛑 VERIFIQUE ESTE PAQUETE

import edu.tecnm.modelo.Producto;
import edu.tecnm.repository.ProductoRepository; 
import edu.tecnm.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
// @Service("productoServiceJpa") <--- 🛑 ELIMINADA PARA RESOLVER EL CONFLICTO 
//                                     (ESTO HACÍA QUE LA APLICACIÓN FALLARA AL INICIO)
import java.util.List;
import java.util.Optional;

// NOTA: Si esta es la clase que quiere usar, debe ELIMINAR O RENOMBRAR la otra clase
// (ProductoServiceJpa.java) en el otro paquete para que no haya duplicidad.
public class ProductoServiceImpl implements IProductoService { 

    @Autowired
    private ProductoRepository repo; 

    @Override
    public List<Producto> buscarTodosProductos() {
        return repo.findAll();
    }

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
    
    // 🛑 MÉTODO REQUERIDO POR EL FILTRO
    @Override
    public List<Producto> buscarPorTipo(String tipo) {
        return repo.findByTipoIgnoreCase(tipo); 
    }
}