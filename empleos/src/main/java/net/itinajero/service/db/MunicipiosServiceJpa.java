package net.itinajero.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.itinajero.model.Municipio;
import net.itinajero.repository.MunicipioRepository;
import net.itinajero.service.IMunicipiosService;

@Service
@Primary

public class MunicipiosServiceJpa implements IMunicipiosService 
{
	@Autowired
	private MunicipioRepository municipioRepo;
	@Override
	public List<Municipio> buscarTodos()
	{
		return municipioRepo.findAll();
	}
	@Override
	public List<Municipio> buscarPorIdEstado(Integer idEstado) 
	{
		return municipioRepo.findByEstadoId(idEstado);
	}
	

}
