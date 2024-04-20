package net.itinajero.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Vacante;

@Service
public class VacantesServiceImpl implements IVacanteService
{
	private List<Vacante> lista = null;
	public VacantesServiceImpl()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		lista = new LinkedList<Vacante>();
		try
		{
			Vacante vacante1 = new Vacante();
			vacante1.setId(1);
			vacante1.setNombre("Ingeniero Civil");
			vacante1.setDescripcion("Solicitamos Ing. Civil para diseñar puente peatonal.");
			vacante1.setFecha(sdf.parse("08-02-2019"));
			vacante1.setSalario(14000.0);
			vacante1.setDestacado(1);
			vacante1.setImagen("empresa1.png");
			
			Vacante vacante2 = new Vacante();
			vacante2.setId(2);
			vacante2.setNombre("Administrador de Sistemas");
			vacante2.setDescripcion("Solicitamos Administrador de Sistemas para administrar servidores y redes.");
			vacante2.setFecha(sdf.parse("01-03-2024"));
			vacante2.setSalario(18000.0);
			vacante2.setDestacado(0);
			vacante2.setImagen("empresa2.png");

			Vacante vacante3 = new Vacante();
			vacante3.setId(3);
			vacante3.setNombre("Vendedor de Autos");
			vacante3.setDescripcion("Solicitamos Vendedor de Autos para vender autos nuevos y usados.");
			vacante3.setFecha(sdf.parse("05-04-2025"));
			vacante3.setSalario(10000.0);
			vacante3.setDestacado(0);

			Vacante vacante4 = new Vacante();
			vacante4.setId(4);
			vacante4.setNombre("Profesor de Matemáticas");
			vacante4.setDescripcion("Solicitamos Profesor de Matemáticas para impartir clases en escuela secundaria.");
			vacante4.setFecha(sdf.parse("10-05-2026"));
			vacante4.setSalario(13000.0);
			vacante4.setDestacado(1);
			vacante4.setImagen("empresa3.png");
			
			lista.add(vacante1);
			lista.add(vacante2);
			lista.add(vacante3);
			lista.add(vacante4);
		}catch(ParseException e)
		{
			System.out.println("Error: "+e.getMessage());
		}
	}
	public List<Vacante> buscarTodas() 
	{
		return lista;
	}
	public Vacante buscarPorId(Integer idVacante) 
	{
		for(Vacante v : lista)
		{
			if(v.getId()==idVacante)
			{
				return v;
			}
		}
		return null;
	}
	
	public void guardar(Vacante vacante) 
	{
		lista.add(vacante);
	}
	public List<Vacante> buscarDestacadas() 
	{
		return null;
	}
	
	public void eliminar(Integer idVacante)
	{
		
	}

	public List<Vacante> buscarByExample(Example<Vacante> example) 
	{
		return null;
	}
	@Override
	public Page<Vacante> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Page<Vacante> buscarPorCompanyCode(Pageable page, String companyCode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int buscarSolicitud(Integer idVacante) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void eliminarPorCompanyCode(String companyCode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Vacante> buscarTodasPorCompanyCode(String companyCode) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
