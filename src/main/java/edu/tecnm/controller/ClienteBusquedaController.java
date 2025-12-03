 package edu.tecnm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.tecnm.modelo.Cliente;
import edu.tecnm.service.IClienteService;

@Controller
@RequestMapping("/busquedas")
public class ClienteBusquedaController {

    @Autowired
    private IClienteService clienteService;

    // Vista inicial
    @GetMapping
    public String mostrarPagina(Model model) {
        model.addAttribute("seccion", ""); // nada abierto por defecto (ajusta si quieres)
        return "cliente/BusquedasCliente";
    }

    // 1) Nombre exacto (resultado único)
    @GetMapping("/por-nombre")
    public String porNombreExacto(
            @RequestParam(value = "nombre", required = false) String nombre,
            Model model) {

        if (nombre == null || nombre.isBlank()) return "redirect:/busquedas";

        clienteService.buscarPorNombreExacto(nombre.trim())
                .ifPresent(c -> model.addAttribute("resultadoUnico", c));

        model.addAttribute("seccion", "nombre");
        return "cliente/BusquedasCliente";
    }

    // 2) Nombre contiene (lista)
    @GetMapping("/nombre-contiene")
    public String nombreContiene(
            @RequestParam(value="q", required=false) String q,
            Model model){

        if (q == null || q.isBlank()) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarPorNombreContiene(q.trim()));
        model.addAttribute("seccion", "nombre");
        return "cliente/BusquedasCliente";
    }

    // 3) Email exacto (resultado único)
    @GetMapping("/email-exacto")
    public String emailExacto(
            @RequestParam(value="email", required=false) String email,
            Model model) {

        if (email == null || email.isBlank()) return "redirect:/busquedas";

        clienteService.buscarPorEmailExacto(email.trim())
                .ifPresent(c -> model.addAttribute("resultadoUnico", c));

        model.addAttribute("seccion", "email");
        return "cliente/BusquedasCliente";
    }

    // 4) Email termina con (lista)
    @GetMapping("/email-termina")
    public String emailTermina(
            @RequestParam(value="suffix", required=false) String suffix,
            Model model) {

        if (suffix == null || suffix.isBlank()) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarPorEmailTermina(suffix.trim()));
        model.addAttribute("seccion", "email");
        return "cliente/BusquedasCliente";
    }

    // 5) Crédito entre (lista)
    @GetMapping("/credito-entre")
    public String creditoEntre(
            @RequestParam(value="desde", required=false) Double desde,
            @RequestParam(value="hasta", required=false) Double hasta,
            Model model) {

        if (desde == null || hasta == null) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarCreditoEntre(desde, hasta));
        model.addAttribute("seccion", "credito");
        return "cliente/BusquedasCliente";
    }

    // 6) Crédito mayor o igual (lista)
    @GetMapping("/credito-mayor")
    public String creditoMayor(
            @RequestParam(value="minimo", required=false) Double minimo,
            Model model) {

        if (minimo == null) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarCreditoMayorIgual(minimo));
        model.addAttribute("seccion", "credito");
        return "cliente/BusquedasCliente";
    }

    // 7) Solo destacados (lista)
    @GetMapping("/destacados")
    public String soloDestacados(Model model) {
        model.addAttribute("lista", clienteService.buscarDestacados());
        model.addAttribute("seccion", "extras");
        return "cliente/BusquedasCliente";
    }

    // 8) Nombre contiene & crédito mínimo (lista)
    @GetMapping("/nombre-y-credito")
    public String nombreYCredito(
            @RequestParam(value="nombre", required=false) String nombre,
            @RequestParam(value="minimo", required=false) Double minimo,
            Model model) {

        if (nombre == null || nombre.isBlank() || minimo == null) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarNombreYCreditoMin(nombre.trim(), minimo));
        model.addAttribute("seccion", "nombre");
        return "cliente/BusquedasCliente";
    }

    // 9) Foto igual (lista)
    @GetMapping("/foto-igual")
    public String fotoIgual(
            @RequestParam(value="file", required=false) String file,
            Model model) {

        if (file == null || file.isBlank()) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarFotoIgual(file.trim()));
        model.addAttribute("seccion", "extras");
        return "cliente/BusquedasCliente";
    }

    // 10) Destacados con crédito mínimo (lista)
    @GetMapping("/destacados-credito")
    public String destacadosCredito(
            @RequestParam(value="minimo", required=false) Double minimo,
            Model model) {

        if (minimo == null) return "redirect:/busquedas";

        model.addAttribute("lista", clienteService.buscarDestacadosConCreditoMin(minimo));
        model.addAttribute("seccion", "extras");
        return "cliente/BusquedasCliente";
    }

    // 11) Top 5 por crédito (lista)
    @GetMapping("/top5-credito")
    public String top5Credito(Model model) {
        model.addAttribute("lista", clienteService.top5PorCredito());
        model.addAttribute("seccion", "credito");
        return "cliente/BusquedasCliente";
    }
}
