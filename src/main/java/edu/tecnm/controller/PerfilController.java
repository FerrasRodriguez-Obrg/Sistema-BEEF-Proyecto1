package edu.tecnm.controller;

import edu.tecnm.modelo.Perfil;
import edu.tecnm.repository.PerfilRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfiles")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepo; 

    // ----------------------------------------------------
    // 1. LISTAR PERFILES
    // ----------------------------------------------------
    @GetMapping("/lista")
    public String listarPerfiles(Model model) {
        model.addAttribute("perfiles", perfilRepo.findAll());
        return "perfil/listaPerfiles"; // 🛑 RUTA CORREGIDA
    }

    // ----------------------------------------------------
    // 2. ABRIR FORMULARIO (NUEVO/EDICIÓN)
    // ----------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoPerfil(Perfil perfil) {
        return "perfil/formPerfil"; // 🛑 RUTA CORREGIDA
    }

    @GetMapping("/editar/{id}")
    public String editarPerfil(@PathVariable("id") Integer id, Model model) {
        Perfil perfil = perfilRepo.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("ID de Perfil inválido:" + id));
        model.addAttribute("perfil", perfil);
        return "perfil/formPerfil"; // 🛑 RUTA CORREGIDA
    }

    // ----------------------------------------------------
    // 3. GUARDAR PERFIL
    // ----------------------------------------------------
    @PostMapping("/guardar")
    public String guardarPerfil(@ModelAttribute Perfil perfil, RedirectAttributes attributes) {
        perfilRepo.save(perfil);
        attributes.addFlashAttribute("msg", "Perfil '" + perfil.getPerfil() + "' guardado con éxito.");
        return "redirect:/perfiles/lista";
    }
}