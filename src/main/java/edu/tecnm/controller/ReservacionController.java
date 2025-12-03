package edu.tecnm.controller;

import edu.tecnm.modelo.*; 
import edu.tecnm.service.*; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/reservaciones")
public class ReservacionController {

	private static final String ESTADO_AUSENTE = "AUSENTE";
	private static final String ESTADO_CONFIRMADA = "CONFIRMADA";

	// Inyección de Servicios
	@Autowired
	private IReservacionService serviceReservaciones;
	@Autowired
	private IMesaService serviceMesas;
	@Autowired
	private IEmpleadoService serviceEmpleados;
	@Autowired
	private IClienteService serviceClientes;
	@Autowired
	private IPedidoService servicePedido;

	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("clientes", serviceClientes.buscarTodosClientes());
		model.addAttribute("mesas", serviceMesas.buscarTodas());
		model.addAttribute("empleados", serviceEmpleados.buscarTodos());
	}

	// ----------------------------------------------------
	// 1. MUESTRA LA LISTA (MODIFICADO PARA FILTROS Y FECHA POR DEFECTO)
	// ----------------------------------------------------
	@GetMapping("/index")
	public String mostrarIndex(
			Model model,
			@RequestParam(required = false) String clienteNombre,
			@RequestParam(required = false) String estado,
			@RequestParam(required = false) String fechaCitaStr) {

		List<Reservacion> reservaciones;
		LocalDate fechaCita = null; 

		if (fechaCitaStr != null && !fechaCitaStr.isEmpty()) {
			try {
				fechaCita = LocalDate.parse(fechaCitaStr); 
			} catch (Exception e) {
				System.err.println("Error al parsear la fecha: " + fechaCitaStr);
			}
		}

		// 🛑 Lógica para determinar si el usuario aplicó algún filtro
		if (clienteNombre != null || estado != null || fechaCita != null) {
			// Carga filtrada: Usa el método de filtros con la fecha (puede ser null)
			reservaciones = serviceReservaciones.buscarPorFiltros(clienteNombre, estado, fechaCita);
			
			// Mantener los valores de búsqueda en el modelo para que se pre-seleccionen en el formulario
			model.addAttribute("clienteNombre", clienteNombre);
			model.addAttribute("estado", estado);
			model.addAttribute("fechaCita", fechaCitaStr); 

		} else {
			// 🛑 CARGA POR DEFECTO: Mostrar solo las reservaciones desde hoy en adelante
			reservaciones = serviceReservaciones.buscarReservacionesDesdeHoy(LocalDate.now());
			
			// Nota: Los campos de filtro en la vista quedarán vacíos/por defecto.
		}

		model.addAttribute("reservaciones", reservaciones);
		model.addAttribute("menuActive", "reservaciones");

		// Hora y fecha actual del servidor para la lógica de Confirmación en la vista
		model.addAttribute("nowDateTime", LocalDateTime.now());

		return "reservaciones/ListaReservaciones";
	}

	@GetMapping("/nuevo")
	public String crear(Reservacion reservacion, Model model) {
		reservacion.setEstado(ESTADO_AUSENTE);
		model.addAttribute("reservacion", reservacion);
		model.addAttribute("menuActive", "reservaciones");

		return "reservaciones/FormularioReservacion";
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Integer idReservacion, Model model) {

		Reservacion reservacion = serviceReservaciones.buscarPorId(idReservacion);

		if (reservacion == null) {
			return "redirect:/reservaciones/index";
		}

		model.addAttribute("reservacion", reservacion);
		model.addAttribute("menuActive", "reservaciones");

		return "reservaciones/FormularioReservacion";
	}

	@PostMapping("/save")
	public String guardar(@ModelAttribute Reservacion reservacion, BindingResult result,
			RedirectAttributes attributes) {

		if (result.hasErrors()) {
			return "reservaciones/FormularioReservacion";
		}

		if (reservacion.getId() == null || reservacion.getId() == 0) {
			reservacion.setId(null);
		}

		// 2. Re-enlazar Cliente, Mesa, Empleado
		if (reservacion.getCliente() != null && reservacion.getCliente().getId() != null) {
			Cliente clienteCompleto = serviceClientes.buscarPorIdCliente(reservacion.getCliente().getId());
			reservacion.setCliente(clienteCompleto);
		}

		if (reservacion.getMesaAsignada() != null && reservacion.getMesaAsignada().getIdMesa() != null) {
			Mesa mesaCompleta = serviceMesas.buscarMesaPorId(reservacion.getMesaAsignada().getIdMesa());
			reservacion.setMesaAsignada(mesaCompleta);
		}

		if (reservacion.getEmpleadoAsignado() != null && reservacion.getEmpleadoAsignado().getId() != null) {
			Empleado empleadoCompleto = serviceEmpleados.buscarPorId(reservacion.getEmpleadoAsignado().getId());
			reservacion.setEmpleadoAsignado(empleadoCompleto);
		}

		// 3. Establecer estado inicial si es nuevo
		if (reservacion.getId() == null) {
			reservacion.setEstado(ESTADO_AUSENTE);
		}

		serviceReservaciones.guardar(reservacion);
		attributes.addFlashAttribute("msg", "La reservación se ha guardado con éxito!");

		return "redirect:/reservaciones/index";
	}

	@PostMapping("/confirmar/{id}")
	public String confirmarAsistencia(@PathVariable("id") Integer idReservacion, RedirectAttributes attributes) {
		Reservacion reserva = serviceReservaciones.buscarPorId(idReservacion);

		if (reserva == null) {
			attributes.addFlashAttribute("error", "Reserva no encontrada.");
			return "redirect:/reservaciones/index";
		}

		if (!ESTADO_AUSENTE.equals(reserva.getEstado())) {
			attributes.addFlashAttribute("error", "La reserva #" + idReservacion + " ya fue Confirmada/Cancelada.");
			return "redirect:/reservaciones/index";
		}

		// LÓGICA CLAVE: CREACIÓN DEL PEDIDO
		try {
			Pedido nuevoPedido = new Pedido();
			nuevoPedido.setCliente(reserva.getCliente());
			nuevoPedido.setFecha(LocalDate.now());
			nuevoPedido.setTotal(0.00);
			nuevoPedido.setDetalles(new ArrayList<>());

			// Guarda el pedido para obtener su ID
			nuevoPedido = servicePedido.guardarPedido(nuevoPedido);

			// Vincula el Pedido a la Reservación y actualiza el estado
			reserva.setEstado(ESTADO_CONFIRMADA);
			reserva.setPedidoAsociado(nuevoPedido);
			serviceReservaciones.guardar(reserva);

			attributes.addFlashAttribute("msg",
					"Asistencia confirmada. Pedido #" + nuevoPedido.getId() + " CREADO. Inicie la captura de productos.");

			// Redirigir al formulario de pedido para que empiecen a agregar productos
			return "redirect:/pedido/editar/" + nuevoPedido.getId();

		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Error al confirmar y crear pedido: " + e.getMessage());
			return "redirect:/reservaciones/index";
		}
	}

	@PostMapping("/delete/{id}")
	public String eliminarReservacion(@PathVariable("id") Integer id, RedirectAttributes attributes) {

		try {
			serviceReservaciones.eliminar(id);
			attributes.addFlashAttribute("msg", "La reservación #" + id + " fue eliminada exitosamente.");
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Error al eliminar la reservación #" + id + ". " + e.getMessage());
		}

		return "redirect:/reservaciones/index";
	}

	@GetMapping("/detalle/{id}")
	public String verDetalleReservacion(@PathVariable("id") Integer idReservacion, Model model) {

		Reservacion reserva = serviceReservaciones.buscarPorId(idReservacion);

		if (reserva == null) {
			return "redirect:/reservaciones/index";
		}

		// 1. Objeto principal: Reservación
		model.addAttribute("reservacion", reserva);

		// 2. Cargar el Pedido asociado (si existe)
		Pedido pedido = servicePedido.buscarPorIdReservacion(idReservacion);

		if (pedido != null) {
			// Forzamos la carga completa del pedido (productos, etc.)
			pedido = servicePedido.buscarPorId(pedido.getId());
			model.addAttribute("pedido", pedido); // 'pedido' es el objeto que contiene la lista de productos
		} else {
			model.addAttribute("pedido", null);
		}

		// Retorna la plantilla correcta: templates/reservaciones/Detalle_reservacion.html
		return "reservaciones/Detalle_reservacion";
	}
}