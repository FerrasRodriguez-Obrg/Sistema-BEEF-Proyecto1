package edu.tecnm.service;

import java.util.List;
import java.time.LocalDate; // 🛑 Importamos LocalDate

import edu.tecnm.modelo.Pedido;

public interface IPedidoService {
    
    // Métodos CRUD existentes
    List<Pedido> buscarTodosPedido();

    // 🛑 Este es el lugar correcto para este método
    Pedido guardarPedido(Pedido pedido); 

    Pedido buscarPorId(Integer id);

    Pedido buscarPorIdReservacion(Integer idReservacion);
    
    void eliminar(Integer id);

    // ******************************************************
    // 🏆 MÉTODOS AGREGADOS PARA EL FILTRO DE BÚSQUEDA
    // ******************************************************
    
    // Búsqueda 1: Por Fecha Y Cliente
    List<Pedido> buscarPorFechaYCliente(LocalDate fecha, Integer idCliente);
    
    // Búsqueda 2: Solo por Fecha
    List<Pedido> buscarPorFecha(LocalDate fecha);
    
    // Búsqueda 3: Solo por Cliente
    List<Pedido> buscarPorCliente(Integer idCliente);
}