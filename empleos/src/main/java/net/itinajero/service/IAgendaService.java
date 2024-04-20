package net.itinajero.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.itinajero.model.Agenda;

public interface IAgendaService 
{
	void guardar(Agenda agenda);
	void eliminar(Integer idAgenda);
	List<Agenda> buscarTodas();
	Page<Agenda> buscarPorCompanyCode(Pageable page,String companyCode);
	Agenda buscarPorid(Integer idVacante);
	void eliminarPorIdRH(Integer idRH);
}
