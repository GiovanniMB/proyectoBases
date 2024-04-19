package net.itinajero.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.itinajero.model.Estado;
import net.itinajero.repository.EstadoRepository;
import net.itinajero.service.IEstadosService;

@Service
@Primary
public class EstadosServiceJpa implements IEstadosService 
{
	@Autowired
	private EstadoRepository estadosRepo;

	@Override
	public List<Estado> buscarTodos() 
	{
		return estadosRepo.findAll();
	}

}
