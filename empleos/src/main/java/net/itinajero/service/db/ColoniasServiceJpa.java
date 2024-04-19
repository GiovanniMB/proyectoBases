package net.itinajero.service.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.itinajero.model.Colonia;
import net.itinajero.repository.ColoniasRepository;
import net.itinajero.service.IColoniasService;

@Service
@Primary
public class ColoniasServiceJpa implements IColoniasService 
{
	@Autowired
	private ColoniasRepository coloniasRepo;
	@Override
	public List<Colonia> buscarTodas() 
	{
		return coloniasRepo.findAll();
	}

	@Override
	public List<Colonia> buscarPorIdMunicipio(Integer idMunicipio)
	{
		return coloniasRepo.findByMunicipioId(idMunicipio);
	}

}
