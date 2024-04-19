package net.itinajero.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.itinajero.model.Solicitud;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> 
{
	Page<Solicitud> findByVacanteEmpresaCompanyCode(Pageable pageable, String companyCode);
	
	@Query("SELECT s FROM Solicitud s " +
		       "INNER JOIN s.vacante v " +
		       "WHERE s.id NOT IN (SELECT a.solicitud.id FROM Agenda a) " +
		       "AND v.empresa.companyCode = :companyCode")
		Page<Solicitud> findSolicitudesNoAgendadasByCompanyCode(Pageable pageable, @Param("companyCode") String companyCode);
	 @Query("SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = :idVacante AND s.usuario.id = :idUsuario")
	    int countByVacanteAndUsuario(@Param("idVacante") Integer idVacante, @Param("idUsuario") Integer idUsuario);
}
