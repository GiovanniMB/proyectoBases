package net.itinajero.service;

import java.util.List;

import net.itinajero.model.Municipio;

public interface IMunicipiosService 
{
	List<Municipio> buscarTodos();
	List<Municipio> buscarPorIdEstado(Integer idEstado);
}
