package net.itinajero.service;

import java.util.List;

import net.itinajero.model.Colonia;

public interface IColoniasService 
{
	List<Colonia> buscarTodas();
	List<Colonia> buscarPorIdMunicipio(Integer idMunicipio);
}
