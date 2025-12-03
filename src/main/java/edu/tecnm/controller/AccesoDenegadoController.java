// src/main/java/edu/tecnm/controller/AccesoDenegadoController.java

package edu.tecnm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesoDenegadoController {

    @GetMapping("/acceso-denegado")
    public String mostrarAccesoDenegado() {
        return "acceso_denegado"; 
    }
}