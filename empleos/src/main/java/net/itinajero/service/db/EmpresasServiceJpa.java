package net.itinajero.service.db;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import net.itinajero.model.Empresa;
import net.itinajero.repository.EmpresasRepository;
import net.itinajero.service.IEmpresasService;

@Service
@Primary
public class EmpresasServiceJpa implements IEmpresasService 
{
	@Autowired
	private EmpresasRepository empresasRepo;
	
	
	public void guardar(Empresa empresa) 
	{
		empresasRepo.save(empresa);
	}

	public void eliminar(String companyCode) 
	{
		empresasRepo.deleteById(companyCode);
	}

	public List<Empresa> buscarTodos() 
	{
		return empresasRepo.findAll();
	}

	public Empresa buscarPorCompanyCode(String companyCode) 
	{
		return empresasRepo.findBycompanyCode(companyCode);
	}

	public List<Empresa> buscarEmpresasnotnull() 
	{
		return empresasRepo.findByHisEmpresaIsNotNull();
	}
	
}
