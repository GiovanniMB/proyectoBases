package net.itinajero.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Solicitud;
import net.itinajero.repository.SolicitudesRepository;
import net.itinajero.service.ISolicitudesService;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService 
{
	@Autowired
	private SolicitudesRepository SolicitudesRepo;

	public void guardar(Solicitud solicitud) 
	{
		SolicitudesRepo.save(solicitud);
	}

	public void eliminar(Integer idSolicitud) 
	{
		SolicitudesRepo.deleteById(idSolicitud);
	}

	public List<Solicitud> buscarTodas() 
	{
		return SolicitudesRepo.findAll();
	}

	public Solicitud buscarPorId(Integer idSolicitud) 
	{
		Optional<Solicitud> optional = SolicitudesRepo.findById(idSolicitud);
		if(optional.isPresent())
		{
			return optional.get();
		}
		return null;
	}

	public Page<Solicitud> buscarTodas(Pageable page) 
	{
		return SolicitudesRepo.findAll(page);
	}

	public Page<Solicitud> buscarPorCompanyCode(Pageable page, String companyCode) 
	{
		return SolicitudesRepo.findByVacanteEmpresaCompanyCode(page, companyCode);
	}

	public Page<Solicitud> buscarPorCompanyCodeAndNotA(Pageable page, String companyCode)
	{
		return SolicitudesRepo.findSolicitudesNoAgendadasByCompanyCode(page, companyCode);
	}

	public int buscarExistente(Integer idVacante, Integer idUsuario) 
	{
		return SolicitudesRepo.countByVacanteAndUsuario(idVacante, idUsuario);
	}

}
