package edu.tecnm.controller;

import edu.tecnm.modelo.Mesa;
import edu.tecnm.service.IMesaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private IMesaService serviceMesas;

    /**
     * Muestra la lista de todas las mesas.
     * Mapeado a la URL: /mesas/lista
     */
    @GetMapping("/lista")
    public String mostrarListaMesas(Model model) {
        
        List<Mesa> mesas = serviceMesas.buscarTodas();
        model.addAttribute("mesas", mesas);
        model.addAttribute("menuActive", "mesas");
        
        return "mesas/listaMesas"; 
    }
    
    /**
     * Muestra el formulario vacío para crear una nueva mesa.
     */
    @GetMapping("/nuevo")
    public String crear(Mesa mesa, Model model) {
        model.addAttribute("mesa", new Mesa());
        return "mesas/formularioMesa"; 
    }
    
    /**
     * Muestra el formulario para editar una mesa existente.
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer idMesa, Model model) {
        Mesa mesa = serviceMesas.buscarMesaPorId(idMesa);
        
        if (mesa == null) {
             return "redirect:/mesas/lista";
        }
        
        model.addAttribute("mesa", mesa);
        return "mesas/formularioMesa";
    }
    
    /**
     * Procesa la solicitud para guardar una mesa (crear o actualizar).
     */
    @PostMapping("/guardar")
    public String guardar(Mesa mesa, RedirectAttributes attributes) {
        
        // Si es una creación, el ID será nulo para forzar el INSERT
        if (mesa.getIdMesa() != null && mesa.getIdMesa() == 0) {
            mesa.setIdMesa(null);
        }
        
        serviceMesas.guardar(mesa);
        attributes.addFlashAttribute("msg", "Mesa guardada con éxito.");
        
        return "redirect:/mesas/lista";
    }

    /**
     * Elimina una mesa por su ID.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer idMesa, RedirectAttributes attributes) {
        try {
            serviceMesas.eliminar(idMesa);
            attributes.addFlashAttribute("msg", "Mesa eliminada con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar la mesa. Asegúrese de que no tenga reservaciones asociadas.");
        }
        return "redirect:/mesas/lista";
    }
}