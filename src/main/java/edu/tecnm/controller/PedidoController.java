package edu.tecnm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType; 
import org.thymeleaf.context.Context; 

import edu.tecnm.modelo.*;
import edu.tecnm.service.*;
import edu.tecnm.util.PdfGeneratorService;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList; 
import java.util.Iterator; 

@Controller
@RequestMapping("/pedido")
@SessionAttributes("pedidoEnSesion") 
public class PedidoController {

    @Autowired
    @Qualifier("pedidoServiceJpa")
    private IPedidoService servicePedido;

    @Autowired
    private IClienteService serviceCliente; 
    
    @Autowired
    @Qualifier("productoServiceJpa") 
    private IProductoService serviceProducto; 
    
    @Autowired
    private PdfGeneratorService pdfGeneratorService;


    // ******************************************************
    // 🛒 GESTIÓN DEL CARRITO (SESIÓN)
    // ******************************************************
    
    // Inicializa el objeto Pedido si no existe en la sesión
    @ModelAttribute("pedidoEnSesion")
    public Pedido iniciarPedido() {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setDetalles(new ArrayList<>());
        nuevoPedido.setTotal(0.0);
        return nuevoPedido;
    }

    // RUTA: /pedido/agregar/{id}
    @GetMapping("/agregar/{id}")
    public String agregarProductoAPedido(
        @PathVariable("id") Integer idProducto, 
        @ModelAttribute("pedidoEnSesion") Pedido pedidoEnSesion,
        RedirectAttributes attributes) {
        
        Producto producto = serviceProducto.buscarPorIdProducto(idProducto);
        
        if (producto != null) {
            // Lógica simple de carrito: Agrega 1 unidad.
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(1);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio());
            detalle.setPedido(pedidoEnSesion);

            pedidoEnSesion.getDetalles().add(detalle);
            
            // Recalcula el total de la sesión
            double nuevoTotal = pedidoEnSesion.getTotal() + detalle.getSubtotal();
            pedidoEnSesion.setTotal(nuevoTotal);

            attributes.addFlashAttribute("msg", "✅ ¡" + producto.getNombre() + " agregado! Total: $" + String.format("%.2f", nuevoTotal));
        } else {
             attributes.addFlashAttribute("error", "⛔ Producto no encontrado.");
        }
        
        return "redirect:/menucliente"; 
    }
    
    // 🛑 NUEVO: RUTA PARA ELIMINAR UN ITEM DEL CARRITO
    @GetMapping("/eliminarItem/{idProducto}")
    public String eliminarItemPedido(
        @PathVariable("idProducto") Integer idProducto,
        @ModelAttribute("pedidoEnSesion") Pedido pedidoEnSesion,
        RedirectAttributes attributes) {

        Iterator<DetallePedido> iterator = pedidoEnSesion.getDetalles().iterator();
        double totalRemovido = 0.0;
        boolean itemEncontrado = false;

        // Itera sobre la lista de detalles y elimina la primera coincidencia
        while (iterator.hasNext()) {
            DetallePedido detalle = iterator.next();
            if (detalle.getProducto() != null && detalle.getProducto().getId().equals(idProducto)) {
                totalRemovido = detalle.getSubtotal();
                iterator.remove(); // Remueve el objeto de la lista
                itemEncontrado = true;
                break; // Solo removemos una unidad para simplificar
            }
        }

        if (itemEncontrado) {
            // Recalcula el total
            pedidoEnSesion.setTotal(pedidoEnSesion.getTotal() - totalRemovido);
            attributes.addFlashAttribute("msg", "Item eliminado del pedido.");
        } else {
            attributes.addFlashAttribute("error", "Error: Item no encontrado en el carrito.");
        }

        // Redirige a la vista del carrito
        return "redirect:/pedido/carrito";
    }

    // 🛑 RUTA: /pedido/carrito
    @GetMapping("/carrito")
    public String verCarrito(@ModelAttribute("pedidoEnSesion") Pedido pedido, Model model) {
        List<Cliente> clientes = serviceCliente.buscarTodosClientes(); 
        model.addAttribute("listaClientes", clientes); 
        return "pedido/carrito"; 
    }
    
    // 🛑 RUTA: /pedido/confirmar
    @PostMapping("/confirmar")
    public String confirmarPedido(
        @ModelAttribute("pedidoEnSesion") Pedido pedidoEnSesion, 
        @RequestParam("idCliente") Integer idCliente, 
        SessionStatus status, 
        RedirectAttributes attributes) {
        
        Cliente clienteSeleccionado = serviceCliente.buscarPorIdCliente(idCliente);
        
        if (clienteSeleccionado == null) {
            attributes.addFlashAttribute("error", "⛔ Error: Debe seleccionar un cliente válido para confirmar el pedido.");
            return "redirect:/pedido/carrito";
        }
        
        pedidoEnSesion.setCliente(clienteSeleccionado);
        
        Pedido pedidoGuardado = procesarYGuardarPedido(pedidoEnSesion);
        
        status.setComplete(); 
        
        attributes.addFlashAttribute("msg", "¡Pedido #" + pedidoGuardado.getId() + " confirmado y guardado para " + clienteSeleccionado.getNombre() + "!");
        return "redirect:/pedido/ver/" + pedidoGuardado.getId();
    }


    // ******************************************************
    // --- LÓGICA DE PERSISTENCIA (Centralizada) ---
    // ******************************************************
    
    private Pedido procesarYGuardarPedido(Pedido pedido) {
        if (pedido.getId() == null || pedido.getId() == 0) { pedido.setId(null); }
        
        if (pedido.getCliente() != null && pedido.getCliente().getId() != null) {
            Cliente cliente = serviceCliente.buscarPorIdCliente(pedido.getCliente().getId());
            pedido.setCliente(cliente); 
        }
        
        if (pedido.getDetalles() != null) {
             double totalCalculado = 0.0;
             List<DetallePedido> detallesValidos = new ArrayList<>();
             
             for (DetallePedido detalle : pedido.getDetalles()) {
                if (detalle.getProducto() == null || detalle.getProducto().getId() == null || detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    continue; 
                }
                Producto producto = serviceProducto.buscarPorIdProducto(detalle.getProducto().getId()); 
                detalle.setProducto(producto);
                detalle.setPrecioUnitario(producto.getPrecio()); 
                double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
                detalle.setSubtotal(subtotal);
                detalle.setPedido(pedido);
                totalCalculado += subtotal;
                detallesValidos.add(detalle);
             }
             
             pedido.setDetalles(detallesValidos);
             pedido.setTotal(totalCalculado);
             pedido.setFecha(LocalDate.now());
        }
        
        return servicePedido.guardarPedido(pedido);
    }
    
    // ******************************************************
    // --- MÉTODOS DE ADMINISTRACIÓN (Existentes) ---
    // ******************************************************

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer idPedido, Model model) {
        Pedido pedido = servicePedido.buscarPorId(idPedido); 
        if (pedido == null) { return "redirect:/pedido/lista"; }
        model.addAttribute("clientes", serviceCliente.buscarTodosClientes());
        model.addAttribute("productos", serviceProducto.buscarTodosProductos()); 
        model.addAttribute("pedido", pedido);
        model.addAttribute("menuActive", "pedidos");
        return "pedido/formularioPedido"; 
    }

    // 🛑 RUTA DE LISTA CON FILTROS (Mantenemos la implementación anterior que funciona)
    @GetMapping({"/lista"})
    public String mostrarListaPedido(Model model,
                                     @RequestParam(name = "fecha", required = false) String fechaStr,
                                     @RequestParam(name = "idCliente", required = false) Integer idCliente) {
        
        List<Pedido> pedidos;
        LocalDate fechaFiltro = null;
        
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                fechaFiltro = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                System.err.println("Error al parsear la fecha: " + fechaStr);
            }
        }

        try {
            // Asumiendo que has implementado estos métodos en IPedidoService:
            if (fechaFiltro != null && idCliente != null) {
                pedidos = servicePedido.buscarPorFechaYCliente(fechaFiltro, idCliente);
            } else if (fechaFiltro != null) {
                pedidos = servicePedido.buscarPorFecha(fechaFiltro);
            } else if (idCliente != null) {
                pedidos = servicePedido.buscarPorCliente(idCliente);
            } else {
                pedidos = servicePedido.buscarTodosPedido();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la lista de pedidos con filtro: " + e.getMessage());
            pedidos = new ArrayList<>();
        }
        
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("fechaFiltro", fechaStr); 
        model.addAttribute("idClienteFiltro", idCliente); 
        model.addAttribute("listaClientes", serviceCliente.buscarTodosClientes());
        model.addAttribute("menuActive", "pedidos"); 
        return "pedido/listaPedidos";
    }

    @GetMapping("/nuevo") 
    public String nuevo(Model model){
        model.addAttribute("clientes", serviceCliente.buscarTodosClientes());
        model.addAttribute("productos", serviceProducto.buscarTodosProductos()); 
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setDetalles(new ArrayList<>()); 
        model.addAttribute("pedido", nuevoPedido);
        return "pedido/formularioPedido"; 
    }
    
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pedido pedido, RedirectAttributes attributes) {
        Pedido pedidoGuardado = procesarYGuardarPedido(pedido);
        attributes.addFlashAttribute("msg", "Pedido guardado/actualizado correctamente.");
        return "redirect:/pedido/ver/" + pedidoGuardado.getId(); 
    }

    @PostMapping("/guardar-y-generar")
    public String guardarYGenerarTicket(@ModelAttribute Pedido pedido, RedirectAttributes attributes) {
        Pedido pedidoGuardado = procesarYGuardarPedido(pedido);
        attributes.addFlashAttribute("msg", "Pedido #" + pedidoGuardado.getId() + " guardado.");
        attributes.addFlashAttribute("generarTicket", true); 
        return "redirect:/pedido/ver/" + pedidoGuardado.getId(); 
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<byte[]> generarTicketPdf(@PathVariable("id") Integer idPedido) { 
        Pedido pedido = servicePedido.buscarPorId(idPedido);
        if (pedido == null) { return ResponseEntity.notFound().build(); }
        if (pedido.getDetalles() != null) { pedido.getDetalles().size(); }
        
        Context context = new Context();
        context.setVariable("pedido", pedido);
        byte[] pdfBytes = pdfGeneratorService.generatePdf("pedido/ticketPdf", context);

        if (pdfBytes.length == 0) { return ResponseEntity.internalServerError().body(null); }

        String filename = "TICKET_THEBEEF_" + pedido.getId() + "_" + LocalDate.now() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF); 
        headers.setContentLength(pdfBytes.length); 
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\""); 
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes); 
    }

    @GetMapping("/ver/{id}")
    public String verDetallePedido(@PathVariable("id") Integer idPedido, Model model) {
        Pedido pedido = servicePedido.buscarPorId(idPedido); 
        if (pedido == null) { return "redirect:/pedido/lista"; }
        model.addAttribute("pedido", pedido);
        model.addAttribute("menuActive", "pedidos");
        return "pedido/detallePedido"; 
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable("id") Integer idPedido, RedirectAttributes attributes) {
        try {
            servicePedido.eliminar(idPedido); 
            attributes.addFlashAttribute("msg", "El pedido #" + idPedido + " fue eliminado exitosamente.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el pedido #" + idPedido + ". Es posible que el pedido no exista.");
        }
        return "redirect:/pedido/lista";
    }
}