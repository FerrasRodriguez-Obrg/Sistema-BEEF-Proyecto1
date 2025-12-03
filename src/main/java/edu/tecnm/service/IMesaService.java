package edu.tecnm.service;

import edu.tecnm.modelo.Mesa;
import java.util.List;

public interface IMesaService {
    
    // Métodos esenciales
    List<Mesa> buscarTodas();
    Mesa buscarMesaPorId(Integer id);
    Mesa guardar(Mesa mesa);
    void eliminar(Integer id);
}