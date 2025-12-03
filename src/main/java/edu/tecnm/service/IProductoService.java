package edu.tecnm.service;

import java.util.List;
import edu.tecnm.modelo.Producto;

public interface IProductoService {

    List<Producto> buscarTodosProductos();

    Producto buscarPorIdProducto(Integer idProducto);
    
    // 🛑 MÉTODO REQUERIDO: Filtrado por tipo
    List<Producto> buscarPorTipo(String tipo); 

    Producto guardarProducto(Producto producto);
    
    void eliminarProducto(Integer id); 
}