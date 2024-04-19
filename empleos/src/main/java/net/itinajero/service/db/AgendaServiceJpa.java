package net.itinajero.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Agenda;
import net.itinajero.repository.AgendaRepository;
import net.itinajero.service.IAgendaService;

@Service
public class AgendaServiceJpa implements IAgendaService 
{
	@Autowired
	private AgendaRepository agendaRepo;
	
	public void guardar(Agenda agenda) 
	{	
		agendaRepo.save(agenda);
	}

	public void eliminar(Integer idAgenda) 
	{
		agendaRepo.deleteById(idAgenda);
	}

	public List<Agenda> buscarTodas() 
	{
		return agendaRepo.findAll();
	}

	public Page<Agenda> buscarPorCompanyCode(Pageable page, String companyCode) 
	{
		
		return agendaRepo.findBySolicitudVacanteEmpresaCompanyCode(page, companyCode);
	}

	public Agenda buscarPorid(Integer idVacante) {
		Optional<Agenda> optional = agendaRepo.findById(idVacante);
		if(optional.isPresent())
		{
			return optional.get();
		}
		return null;
	}
}
