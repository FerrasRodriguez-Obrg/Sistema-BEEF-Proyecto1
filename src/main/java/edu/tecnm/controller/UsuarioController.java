// src/main/java/edu/tecnm/controller/UsuarioController.java

package edu.tecnm.controller;

import edu.tecnm.modelo.Usuario;
import edu.tecnm.repository.PerfilRepository;
import edu.tecnm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

// Importación necesaria para BCrypt
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo; 

    @Autowired
    private PerfilRepository perfilRepo;

    // Inyectar el codificador
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ----------------------------------------------------
    // 1. LISTAR USUARIOS
    // ----------------------------------------------------
    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "usuario/listaUsuarios";
    }

    // ----------------------------------------------------
    // 2. ABRIR FORMULARIO (NUEVO/EDICIÓN)
    // ----------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoUsuario(Usuario usuario, Model model) {
        model.addAttribute("perfilesDisponibles", perfilRepo.findAll());
        return "usuario/formUsuario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioRepo.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("ID de Usuario inválido:" + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("perfilesDisponibles", perfilRepo.findAll());
        return "usuario/formUsuario";
    }

    // ----------------------------------------------------
    // 3. GUARDAR USUARIO (CON CODIFICACIÓN BCrypt)
    // ----------------------------------------------------
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        
        String plainPassword = usuario.getPassword();

        if (usuario.getId() == null) {
            // --- A. USUARIO NUEVO ---
            usuario.setFechaRegistro(LocalDate.now()); 
            usuario.setEstatus(true);
            usuario.setPassword(passwordEncoder.encode(plainPassword));
            
        } else {
            // --- B. USUARIO EXISTENTE (EDICIÓN) ---
            if (plainPassword != null && !plainPassword.isEmpty()) {
                // Codificar la NUEVA contraseña
                usuario.setPassword(passwordEncoder.encode(plainPassword));
            } else {
                // Si la contraseña estaba VACÍA (no se quiere cambiar)
                Usuario usuarioExistente = usuarioRepo.findById(usuario.getId()).orElse(null);
                if (usuarioExistente != null) {
                    usuario.setPassword(usuarioExistente.getPassword());
                }
            }
        }
        
        usuarioRepo.save(usuario);
        attributes.addFlashAttribute("msg", "Usuario '" + usuario.getUsername() + "' guardado con éxito.");
        return "redirect:/usuarios/lista";
    }
    
    // ----------------------------------------------------
    // 4. ELIMINAR USUARIO (MÉTODO NUEVO)
    // ----------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            // Opcional: Validar que el usuario no se elimine a sí mismo (seguridad)
            // (Lo omitiremos por ahora para mantenerlo simple)
            
            Usuario usuario = usuarioRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("ID de Usuario inválido:" + id));
            
            usuarioRepo.deleteById(id);
            attributes.addFlashAttribute("msg", "Usuario '" + usuario.getUsername() + "' eliminado con éxito.");
        
        } catch (Exception e) {
            // Captura errores (ej. si el usuario está ligado a otras tablas)
            attributes.addFlashAttribute("msg_error", "Error al eliminar el usuario (ID: " + id + ").");
        }
        return "redirect:/usuarios/lista";
    }
}