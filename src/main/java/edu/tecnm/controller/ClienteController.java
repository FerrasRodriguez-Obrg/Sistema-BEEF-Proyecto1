package edu.tecnm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // Importar para manejar 'keyword'
import org.springframework.web.multipart.MultipartFile;

import edu.tecnm.modelo.Cliente;
import edu.tecnm.service.IClienteService;
import edu.tecnm.util.UploadFileHelper;

@Controller
public class ClienteController {

    // inyección de dependencia 
    @Autowired 
    private IClienteService serviceCliente;

    // DEFINIMOS LA RUTA BASE DONDE SE GUARDARÁN LAS IMÁGENES
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static final String RUTA_BASE_IMAGENES = "C:/ruta-externa/clientes/"; 
    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // LISTA Y BÚSQUEDA (Método unificado)
    // Añadimos @RequestParam para recibir la palabra clave de búsqueda
    @GetMapping("/cliente")
    public String mostrarListaClientes (
            Model model,
            @RequestParam(value = "keyword", required = false) String keyword) {
        
        List<Cliente> lista;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Si hay palabra clave, la usamos para la búsqueda genérica
            lista = serviceCliente.search(keyword.trim());
            // Añadimos la palabra clave al modelo para que se mantenga en el input HTML
            model.addAttribute("keyword", keyword); 
        } else {
            // Si no hay palabra clave o está vacía, mostramos la lista completa
            lista = serviceCliente.buscarTodosClientes();
        }
        
        model.addAttribute("clientelista", lista);
        model.addAttribute("menuActive", "clientes"); // Para activar el menú
        return "listaClientes";
    }

    // DETALLE
    @GetMapping("/ver/{id}")
    public String verDetalleCliente(@PathVariable("id") Integer idCliente, Model model) {
        Cliente cliente = serviceCliente.buscarPorIdCliente(idCliente);
        if (cliente == null) return "redirect:/cliente";
        model.addAttribute("cliente", cliente);
        return "cliente/Detalle";
    }

    // CREAR (form vacío)
    @GetMapping("/crear/crear/nuevo")
    public String crearCliente(Cliente cliente, Model model) {
        model.addAttribute("cliente", new Cliente()); 
        return "cliente/formCliente";
    }

    // GUARDAR (crear) - CON LÓGICA DE FOTO
    @PostMapping("/guardar")
    public String guardarCliente(
            Cliente cliente, 
            @RequestParam("foto") MultipartFile file) {

        // Si se subió un archivo y no está vacío
        if (file != null && !file.isEmpty()) {
            // 1. Guardar el archivo en la ruta física y obtener el nombre único.
            String nombreImagen = UploadFileHelper.guardarArchivo(file, RUTA_BASE_IMAGENES); 
            
            if (nombreImagen != null) {
                // 2. Guardar el nombre único del archivo en la base de datos (entidad Cliente).
                cliente.setFotocliente(nombreImagen); 
            }
        }
        
        serviceCliente.guardarCliente(cliente);
        System.out.println("Cliente guardado: " + cliente.getFotocliente());
        return "redirect:/cliente";
    }

    // EDITAR (cargar form con datos)
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable("id") Integer idCliente, Model model) {
        Cliente cliente = serviceCliente.buscarPorIdCliente(idCliente);
        if (cliente == null) return "redirect:/cliente";
        model.addAttribute("cliente", cliente);
        return "cliente/formCliente"; 
    }

    // ACTUALIZAR (submit de edición) - CON LÓGICA DE FOTO
    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(
            @PathVariable("id") Integer idCliente, 
            Cliente form,
            @RequestParam("foto") MultipartFile file) {

        Cliente existente = serviceCliente.buscarPorIdCliente(idCliente);
        if (existente == null) return "redirect:/cliente";

        // 1. Manejo y guardado del nuevo archivo
        if (file != null && !file.isEmpty()) {
            // Guardar la nueva foto
            String nombreImagen = UploadFileHelper.guardarArchivo(file, RUTA_BASE_IMAGENES);
            if (nombreImagen != null) {
                existente.setFotocliente(nombreImagen); // Actualiza la foto en la entidad
            }
        }
        
        // 2. Actualiza los demás campos editables
        existente.setNombre(form.getNombre());
        existente.setApellidos(form.getApellidos());
        existente.setEmail(form.getEmail());
        existente.setTelefono(form.getTelefono());
        // Se usa try-catch para evitar errores si los campos numéricos vienen vacíos o mal
        try { existente.setCredito(form.getCredito()); } catch (Exception ignored) {}
        try { existente.setDestacado(form.getDestacado()); } catch (Exception ignored) {}
        
        // 3. Guarda la entidad actualizada
        serviceCliente.guardarCliente(existente);
        return "redirect:/cliente";
    }

    // ELIMINAR
    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Integer idCliente) {
        serviceCliente.eliminarPorId(idCliente);
        return "redirect:/cliente";
    }
}