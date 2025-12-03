package edu.tecnm.controller;

import edu.tecnm.modelo.Producto;
import edu.tecnm.service.IReservacionService;
import edu.tecnm.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.security.Principal; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class InicioController {
    
    @Autowired
    private IReservacionService serviceReservaciones;
    
    @Autowired
    @Qualifier("productoServiceJpa")
    private IProductoService serviceProducto;

    // ----------------------------------------------------
    // 1. PUNTO DE ENTRADA AL DASHBOARD PRINCIPAL (LOGUEADO)
    // ----------------------------------------------------
    
    @GetMapping ({"/dashboard", "/home", "/dashboard.html"})
    public String mostrarDashboardPrincipal(Model model, Principal principal) {
        
        if (principal != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = principal.getName();
            
            String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + ", " + b)
                .orElse("N/A");

            model.addAttribute("usuarioLogueado", username);
            model.addAttribute("rolesLogueado", roles);
        }
        
        model.addAttribute("menuActive", "home");
        return "inicio"; 
    }
    
    // ----------------------------------------------------
    // 2. RUTA DEL MENÚ DIGITAL DEL CLIENTE (PÚBLICO)
    // ----------------------------------------------------
    @GetMapping("/menu/cliente")
    public String mostrarMenuCliente(Model model) {
        List<Producto> productos = serviceProducto.buscarTodosProductos();
        
        model.addAttribute("productos", productos);
        model.addAttribute("menuActive", "menu_cliente");
        
        return "producto/menu_cliente";
    }
    
    // ----------------------------------------------------
    // 3. RUTAS DE ACCESO PÚBLICO / INVITADO
    // ----------------------------------------------------
    
    /**
     * PÁGINA PRINCIPAL / RAIZ: Ahora solo redirige al LOGIN.
     * Si no está autenticado, la seguridad lo envía al /login.
     */
    @GetMapping ("/")
    public String mostrarPaginaPrincipal() {
        // Redirigir al login. Spring Security se encarga de lo demás.
        return "redirect:/login"; 
    }
    
    @GetMapping ("/invitado")
    public String mostrarDashboardInvitado(Model model) {
        model.addAttribute("menuActive", "guest");
        return "invitado_dashboard";
    }
    
    @GetMapping("/invitado/reservaciones")
    public String verReservacionesInvitado(Model model) {
        model.addAttribute("reservaciones", serviceReservaciones.buscarTodas());
        model.addAttribute("esInvitado", true);
        model.addAttribute("menuActive", "guest");
        return "reservaciones/ListaReservaciones";
    }
}