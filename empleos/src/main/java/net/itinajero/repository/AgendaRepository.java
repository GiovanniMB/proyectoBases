package net.itinajero.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda,Integer>
{
	Page<Agenda> findBySolicitudVacanteEmpresaCompanyCode(Pageable pageable, String companyCode);
	void deleteByUsuarioRHId(Integer idRH);
}
