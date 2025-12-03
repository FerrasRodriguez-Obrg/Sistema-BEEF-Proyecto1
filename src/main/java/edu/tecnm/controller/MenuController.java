package edu.tecnm.controller;

import edu.tecnm.modelo.Producto;
import edu.tecnm.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class MenuController {

    // Inyecta el servicio de productos para obtener la lista de la BD
    @Autowired
    @Qualifier("productoServiceJpa") 
    private IProductoService serviceProducto;

    /**
     * Muestra la vista del menú para el cliente, permitiendo el filtrado opcional por tipo.
     * Ruta: /menucliente
     */
    @GetMapping("/menucliente")
    public String mostrarMenuCliente(Model model, 
                                     @RequestParam(name = "tipo", required = false) String tipo) {
        
        List<Producto> productos;
        
        // --- Lógica de Filtrado ---
        if (tipo != null && !tipo.isEmpty()) {
            // Si hay un filtro, usa el método buscarPorTipo (que usa IgnoreCase)
            productos = serviceProducto.buscarPorTipo(tipo);
        } else {
            // Si no hay filtro (Mostrar Todos), carga la lista completa
            productos = serviceProducto.buscarTodosProductos();
        }
        
        // 1. Pasa la lista de productos al HTML bajo el nombre CRÍTICO 'listaProducto'
        model.addAttribute("listaProducto", productos); 
        
        // 2. Pasa el parámetro de filtro actual para que el <select> del HTML sepa qué opción mostrar
        model.addAttribute("param_tipo", tipo); 
        
        return "producto/menu_cliente"; 
    }
}