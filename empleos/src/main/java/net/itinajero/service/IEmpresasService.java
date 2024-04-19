package net.itinajero.service;

import java.util.List;

import net.itinajero.model.Empresa;


public interface IEmpresasService 
{
	void guardar(Empresa empresa);
	void eliminar(String companyCode);
	List<Empresa> buscarTodos();
	Empresa buscarPorCompanyCode(String companyCode);
}
