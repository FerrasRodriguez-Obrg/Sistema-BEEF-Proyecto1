package edu.tecnm.service.jpa;

import edu.tecnm.modelo.Perfil;
import edu.tecnm.modelo.Usuario;
import edu.tecnm.repository.PerfilRepository;
import edu.tecnm.repository.UsuarioRepository;
import edu.tecnm.service.IUsuarioService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; 
import java.util.Optional; // 🛑 Necesario para findById

@Service
public class UsuarioServiceJpa implements IUsuarioService {
	
	@Autowired private UsuarioRepository usuarioRepo;
	@Autowired private PerfilRepository perfilRepo;
	// 🛑 Nota: Aquí se inyectaría el PasswordEncoder cuando lo implementemos.

	// Implementación de la Interfaz IUsuarioService

	@Override
	public List<Usuario> buscarTodos() {
		return usuarioRepo.findAll();
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return usuarioRepo.findByUsername(username);
	}
    
    // 🛑 MÉTODO AÑADIDO: BUSCAR POR ID
    @Override
    public Usuario buscarPorId(Integer id) {
        Optional<Usuario> optional = usuarioRepo.findById(id);
        return optional.orElse(null);
    }


	@Override
	@Transactional 
	public Usuario guardarUsuario(Usuario usuario) {
		
		// Lógica de guardado
		return usuarioRepo.save(usuario);
	}
}