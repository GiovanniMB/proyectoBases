package net.itinajero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Colonia;

public interface ColoniasRepository extends JpaRepository<Colonia, Integer> 
{
	List<Colonia> findByMunicipioId(Integer idMunicipio);
}
