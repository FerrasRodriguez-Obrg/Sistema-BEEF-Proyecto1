package edu.tecnm.controller;

import org.springframework.stereotype.Controller; // Importante
import org.springframework.web.bind.annotation.GetMapping; // Importante
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller // 1. Marca la clase como un controlador
public class LoginController {

    /**
     * Maneja las peticiones GET a /login y devuelve la plantilla de login.
     * * @param error Parámetro opcional para mostrar un mensaje si la autenticación falló.
     * @param model Objeto para pasar datos (como el mensaje de error) a la plantilla.
     * @return El nombre de la plantilla HTML/Thymeleaf (ej: "login.html").
     */
    @GetMapping("/login") // 2. Mapea la petición GET a la URL /login
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        
        // 3. Lógica para manejar el error de autenticación
        // Spring Security redirige a /login?error si las credenciales son incorrectas.
        if (error != null) {
            model.addAttribute("loginError", "Usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");
        }
        
        // 4. Devuelve el nombre de tu archivo HTML (debe estar en src/main/resources/templates/)
        return "login"; 
    }
}