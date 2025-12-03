package edu.tecnm.service.jpa;

import edu.tecnm.modelo.Reservacion;
import edu.tecnm.repository.ReservacionRepository;
import edu.tecnm.service.IReservacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; 
import java.time.LocalDateTime;
import java.time.LocalDate; 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservacionServiceJpa implements IReservacionService {

    @Autowired
    private ReservacionRepository repo;

    private static final String ESTADO_AUSENTE = "AUSENTE";
    private static final String ESTADO_CANCELADA = "CANCELADA";


    @Override
    public Reservacion buscarPorId(Integer id) {
        Optional<Reservacion> optional = repo.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Reservacion> buscarTodas() {
        return repo.findAll();
    }

    @Override
    public Reservacion guardar(Reservacion reservacion) {
        return repo.save(reservacion);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
    
    @Override
    public List<Reservacion> buscarPorFiltros(String clienteNombre, String estado, LocalDate fechaCita) {
        
        String nombre = StringUtils.hasText(clienteNombre) ? clienteNombre : null;
        String est = StringUtils.hasText(estado) ? estado : null;
        
        return repo.findByFiltros(nombre, est, fechaCita);
    }
    
    // 🛑 IMPLEMENTACIÓN DEL MÉTODO DE CARGA POR DEFECTO (NUEVO)
    @Override
    public List<Reservacion> buscarReservacionesDesdeHoy(LocalDate fechaMinima) {
        return repo.findByFechaCitaGreaterThanEqual(fechaMinima);
    }


    @Transactional
    public void cancelarReservacionesExpiradas(int minutosDeGracia) {
        
        List<Reservacion> reservasPendientes = repo.findAll().stream()
            .filter(r -> ESTADO_AUSENTE.equalsIgnoreCase(r.getEstado()))
            .collect(Collectors.toList());

        for (Reservacion r : reservasPendientes) {
            LocalDateTime horaCitaCompleta = LocalDateTime.of(r.getFechaCita(), r.getHoraCita());
            LocalDateTime horaLimite = horaCitaCompleta.plusMinutes(minutosDeGracia);

            if (LocalDateTime.now().isAfter(horaLimite)) {
                r.setEstado(ESTADO_CANCELADA);
                repo.save(r);
                System.out.println("❌ Reserva #" + r.getId() + " cancelada por expiración.");
            }
        }
    }
}