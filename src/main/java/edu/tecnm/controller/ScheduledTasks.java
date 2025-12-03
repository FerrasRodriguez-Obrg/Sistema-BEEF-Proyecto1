package edu.tecnm.controller; // O un paquete llamado 'componentes' o 'tareas'

import edu.tecnm.modelo.Reservacion;
import edu.tecnm.service.IReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {

    @Autowired
    private IReservacionService serviceReservaciones; 

    // Se ejecuta cada 1 minuto (60 segundos)
    @Scheduled(fixedRate = 60000) 
    public void cancelarReservacionesExpiradas() {
        System.out.println("🤖 Ejecutando tarea de cancelación de reservas: " + LocalDateTime.now());
        
        final String ESTADO_AUSENTE = "AUSENTE";
        final String ESTADO_CANCELADA = "CANCELADA";
        final int MINUTOS_DE_GRACIA = 3;

        // 1. Obtener todas las reservas que están en estado AUSENTE
        List<Reservacion> reservasPendientes = serviceReservaciones.buscarTodas()
            .stream()
            .filter(r -> ESTADO_AUSENTE.equalsIgnoreCase(r.getEstado()))
            .collect(Collectors.toList());

        for (Reservacion r : reservasPendientes) {
            
            // 2. Definir la hora límite de la cita (Fecha + Hora de la cita + 3 minutos)
            LocalDateTime horaCitaCompleta = LocalDateTime.of(r.getFechaCita(), r.getHoraCita());
            LocalDateTime horaLimite = horaCitaCompleta.plusMinutes(MINUTOS_DE_GRACIA);

            // 3. Comparar con la hora actual
            if (LocalDateTime.now().isAfter(horaLimite)) {
                
                r.setEstado(ESTADO_CANCELADA);
                serviceReservaciones.guardar(r); // Guardar el cambio de estado
                
                System.out.println("❌ Reserva #" + r.getId() + " cancelada por expiración.");
            }
        }
    }
}