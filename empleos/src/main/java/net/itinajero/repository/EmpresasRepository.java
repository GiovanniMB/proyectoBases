package net.itinajero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Empresa;

public interface EmpresasRepository extends JpaRepository<Empresa,String> 
{
	//Buscar por companyCode
	Empresa findBycompanyCode(String companyCode);
	//Eliminar por companyCode
	void deleteBycompanyCode(String companyCode);
	 List<Empresa> findByHisEmpresaIsNotNull();
}
