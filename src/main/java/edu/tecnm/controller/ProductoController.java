package edu.tecnm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.tecnm.modelo.Producto;
import edu.tecnm.service.IProductoService;
import edu.tecnm.service.IPedidoService;
import edu.tecnm.util.UploadFileHelper;

@Controller
public class ProductoController {

    // Servicios
    @Autowired
    @Qualifier("productoServiceJpa")
    private IProductoService serviceProducto;

    @Autowired  
    private IPedidoService pedidoService;  

    // ----------------------------------------------------
    // LISTA (ADMIN) - FILTRO POR TIPO
    // RUTA: /listaproductos
    // ----------------------------------------------------
    @GetMapping("/listaproductos")
    public String mostrarListaProductos(@RequestParam(required = false) String tipo, Model model) {
        List<Producto> producto;
        String tipoSeleccionado = (tipo != null && !tipo.isEmpty()) ? tipo : null;

        if (tipoSeleccionado != null) {
            // Usa el método corregido (findByTipoIgnoreCase) en el servicio
            producto = serviceProducto.buscarPorTipo(tipoSeleccionado);  
        } else {
            // Muestra todos los productos
            producto = serviceProducto.buscarTodosProductos();
        }
        
        // CRÍTICO: Aseguramos que la variable del modelo sea 'listaProducto'
        model.addAttribute("listaProducto", producto);
        model.addAttribute("param_tipo", tipoSeleccionado); // Mantener el filtro seleccionado
        
        // Atributo para activar el menú de navegación (si aplica)
        model.addAttribute("adminActive", "productos"); 
        
        return "producto/listaProducto";
    }

    // ELIMINADO: La ruta @GetMapping("/productos/menu") ha sido ELIMINADA de este 
    // controlador para evitar el error de mapeo ambiguo y ahora reside en MenuClienteController.

    // ----------------------------------------------------
    // VISTAS Y OPERACIONES CRUD
    // ----------------------------------------------------
    
    @GetMapping("/producto/ver/{id}")
    public String verDetalleProducto(@PathVariable("id") int idProducto, Model model) {
        Producto producto = serviceProducto.buscarPorIdProducto(idProducto);
        model.addAttribute("producto", producto);
        return "producto/DetalleProducto";
    }
    
    @GetMapping("/producto/crear")  
    public String mostrarFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formProducto";
    }

    @PostMapping("/producto/guardar")
    public String guardarProducto(
            Producto producto,
            @RequestParam(name = "imagenFile", required = false) MultipartFile file,
            RedirectAttributes attributes) {

        if (file != null && !file.isEmpty()) {
            // Nota: La ruta absoluta es sensible a cambios de entorno.
            String rutaAbsoluta = "C:/producto/";  
            String nombreImagen = UploadFileHelper.guardarArchivo(file, rutaAbsoluta);
            
            if (nombreImagen != null) {
                producto.setFotoproducto(nombreImagen);
            }
        }
        serviceProducto.guardarProducto(producto);
        attributes.addFlashAttribute("msg", "Producto guardado exitosamente.");
        return "redirect:/listaproductos";
    }

    @GetMapping("/producto/editar/{id}")
    public String editar(@PathVariable Integer id, Model model){
        Producto p = serviceProducto.buscarPorIdProducto(id);
        model.addAttribute("producto", p);
        return "producto/formProducto";
    }

    @PostMapping("/producto/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes attributes){
        serviceProducto.eliminarProducto(id);
        attributes.addFlashAttribute("msg", "Producto eliminado exitosamente.");
        return "redirect:/listaproductos";
    }
}