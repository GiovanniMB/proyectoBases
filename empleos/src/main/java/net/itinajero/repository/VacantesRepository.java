package net.itinajero.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.itinajero.model.Vacante;

public interface VacantesRepository extends JpaRepository<Vacante, Integer> 
{
	List<Vacante> findByEstatus(String estatus);
	List<Vacante> findByDestacadoAndEstatusOrderByIdDesc(int destacado, String estatus);
	List<Vacante> findBySalarioBetweenOrderBySalarioDesc(double s1, double s2);
	List<Vacante> findByEstatusIn(String[] estatus);
	Page<Vacante> findByEmpresaCompanyCode(Pageable pageable, String companyCode);
	@Query("SELECT COUNT(s) FROM Solicitud s WHERE s.vacante.id = :idVacante")
    int countSolicitudesByVacanteId(@Param("idVacante") Integer idVacante);
	void deleteByEmpresaCompanyCode(String companyCode);
	List<Vacante> findByEmpresaCompanyCode(String companyCode);
}
