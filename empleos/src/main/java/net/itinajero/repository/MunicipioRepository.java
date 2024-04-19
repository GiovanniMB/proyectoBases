package net.itinajero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer> 
{
	List<Municipio> findByEstadoId(Integer idEstado);
}
