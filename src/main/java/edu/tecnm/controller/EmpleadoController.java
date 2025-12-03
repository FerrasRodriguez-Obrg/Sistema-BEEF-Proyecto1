package edu.tecnm.controller;

import edu.tecnm.service.IEmpleadoService;
import edu.tecnm.modelo.Empleado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService serviceEmpleados;

    /**
     * Muestra la lista de todos los empleados.
     * Mapeado a la URL: /empleados/lista
     */
    @GetMapping("/lista")
    public String mostrarListaEmpleados(Model model) {
        
        try {
            List<Empleado> empleados = serviceEmpleados.buscarTodos();
            model.addAttribute("empleados", empleados);
        } catch (Exception e) {
            model.addAttribute("empleados", java.util.Collections.emptyList());
            System.err.println("Error al cargar la lista de empleados: " + e.getMessage());
        }

        model.addAttribute("menuActive", "empleados");
        
        // CORRECCIÓN FINAL: Usar el nombre con mayúsculas
        return "empleados/ListaEmpleados"; 
    }
    
    /**
     * Muestra el formulario vacío para crear un nuevo empleado.
     */
    @GetMapping("/nuevo")
    public String crear(Empleado empleado, Model model) {
        model.addAttribute("empleado", new Empleado()); 
        model.addAttribute("menuActive", "empleados");
        return "empleados/formularioEmpleado"; // Asumimos minúscula para el formulario
    }
    
    /**
     * Procesa la solicitud para guardar un nuevo empleado o actualizar uno existente.
     */
    @PostMapping("/guardar")
    public String guardar(Empleado empleado, RedirectAttributes attributes) {
        
        if (empleado.getId() != null && empleado.getId() == 0) {
            empleado.setId(null);
        }
        
        serviceEmpleados.guardar(empleado);
        attributes.addFlashAttribute("msg", "Empleado guardado con éxito.");
        
        return "redirect:/empleados/lista";
    }

    /**
     * Muestra el formulario para editar un empleado existente.
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Empleado empleado = serviceEmpleados.buscarPorId(id);
        
        if (empleado == null) {
             return "redirect:/empleados/lista";
        }
        
        model.addAttribute("empleado", empleado);
        model.addAttribute("menuActive", "empleados");
        return "empleados/formularioEmpleado";
    }
    
    /**
     * Elimina un empleado por su ID.
     */
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes attributes) {
        try {
            serviceEmpleados.eliminar(id);
            attributes.addFlashAttribute("msg", "Empleado eliminado con éxito.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el empleado. Asegúrese de que no tenga reservaciones asignadas.");
        }
        return "redirect:/empleados/lista";
    }
}