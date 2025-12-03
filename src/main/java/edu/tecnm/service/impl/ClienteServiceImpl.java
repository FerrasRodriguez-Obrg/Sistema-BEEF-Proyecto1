package edu.tecnm.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Necesario para la implementación de búsqueda

import org.springframework.stereotype.Service;

import edu.tecnm.modelo.Cliente;
import edu.tecnm.service.IClienteService;


@Service
public class ClienteServiceImpl implements IClienteService{
	
	List<Cliente> listaCliente=null;
	
	public ClienteServiceImpl() {
	this.listaCliente=new LinkedList<>();
		
		Cliente cliente1=new Cliente();
		cliente1.setId(1);
		cliente1.setNombre("Juliette Irina");
		cliente1.setApellidos("Celis Morales");
		cliente1.setCredito(300.00);
		cliente1.setDestacado(0);
		cliente1.setFotocliente("cliente1.jpg");
		cliente1.setTelefono("3322414787");
		cliente1.setEmail("celisirina701@gmail.com");
		
		
		
		Cliente cliente2 = new Cliente();
		cliente2.setId(2);
		cliente2.setNombre("Melvin Jesús");     
		cliente2.setApellidos("Villanueva Gómez");
		cliente2.setCredito(400.00);
		cliente2.setDestacado(3);
		cliente2.setFotocliente("cliente2.png");
		cliente2.setTelefono("7472242787");
		cliente2.setEmail("melvingm@gmail.com");

		
		
		
		Cliente cliente3=new Cliente();
		cliente3.setId(3);
		cliente3.setNombre("Christopher");
		cliente3.setApellidos("Blanquet Mendez");
		cliente3.setCredito(600.00);
		cliente3.setDestacado(2);
		cliente3.setFotocliente("cliente3.jpg");
		cliente3.setTelefono("7474701209");
		cliente3.setEmail("L21520376@chilpancingo.tecnm.mx");
		
		listaCliente.add(cliente1);
		listaCliente.add(cliente2);
		listaCliente.add(cliente3);
		
	}

	@Override
	public List<Cliente> buscarTodosClientes() {
		return listaCliente;
	}
	
	@Override
	public Cliente buscarPorIdCliente(Integer idCliente) {
		for(Cliente cli: listaCliente)
			if(cli.getId()== idCliente)
				return cli;
		return null;
	}

	//Guardar un cliente 
	@Override
	public void guardarCliente(Cliente cliente) {
		listaCliente.add(cliente);
	}

	@Override
	public void eliminarPorId(Integer idCliente) {
		// Implementación pendiente
	}
    
    // ==========================================================
    // MÉTODO DE BÚSQUEDA GENÉRICA (IMPLEMENTACIÓN MANUAL)
    // ==========================================================
    @Override
    public List<Cliente> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return listaCliente; // Devuelve la lista completa si no hay palabra clave
        }
        
        final String lowerCaseKeyword = keyword.toLowerCase().trim();
        
        // Usamos Streams para filtrar la lista en memoria
        return listaCliente.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(lowerCaseKeyword)
                        || c.getApellidos().toLowerCase().contains(lowerCaseKeyword)
                        || c.getTelefono().contains(lowerCaseKeyword) // Teléfono no necesita toLowerCase si solo contiene dígitos
                        || c.getEmail().toLowerCase().contains(lowerCaseKeyword)) // Opcional: buscar también en email
                .collect(Collectors.toList());
    }

    // El resto de los métodos de búsqueda que están pendientes
    
	@Override
	public Optional<Cliente> buscarPorNombreExacto(String nombre) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Cliente> buscarPorNombreExactoLista(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarPorNombreContiene(String q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Cliente> buscarPorEmailExacto(String email) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Cliente> buscarPorEmailTermina(String sufijo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarPorEmailContiene(String fragmento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarCreditoEntre(Double desde, Double hasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarCreditoMayorIgual(Double minimo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarDestacados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarNombreYCreditoMin(String nombre, Double minimo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarFotoIgual(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> buscarDestacadosConCreditoMin(Double minimo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cliente> top5PorCredito() {
		// TODO Auto-generated method stub
		return null;
	}
}