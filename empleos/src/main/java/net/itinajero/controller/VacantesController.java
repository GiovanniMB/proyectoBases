package net.itinajero.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriasService;
import net.itinajero.service.IEmpresasService;
import net.itinajero.service.IVacanteService;
import net.itinajero.util.Utileria;




@Controller
@RequestMapping("/vacantes")
public class VacantesController 
{
	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;
	@Autowired
	private IVacanteService serviceVacantes;
	@Autowired
	@Qualifier("categoriasServiceJpa")
	private ICategoriasService serviceCategorias;
	@Autowired
	@Qualifier("empresasServiceJpa")
	private IEmpresasService serviceEmpresas;
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model)
	{
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante",vacante);
		return "/vacantes/formVacante";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("vacantes", lista);
		return "vacantes/listVacantes";
	}
	

	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page,HttpSession session) 
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		List<Perfil> perfiles = usuario.getPerfiles();
		
		for(int i=0;i<perfiles.size();i++)
		{
			if(perfiles.get(i).getId()==4)
			{
				Page<Vacante> lista = serviceVacantes.buscarPorCompanyCode(page, usuario.getEmpresa().getCompanyCode());
				model.addAttribute("vacantes", lista);
			}
			else
			{
				Page<Vacante> lista = serviceVacantes.buscarTodas(page);
				model.addAttribute("vacantes", lista);
			}
		}
		return "vacantes/listVacantes";
	}
	
	@GetMapping("/create")
	public String crear(Vacante vacante, Model model)
	{
		return "vacantes/formVacante";
	}
	
	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result,
			RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart,
			HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		if (result.hasErrors()) 
		{
			for (ObjectError error: result.getAllErrors())
			{
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}
		if (!multiPart.isEmpty())
		{
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null)
			{ 
				// La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen); 
			}
		}
		vacante.setEmpresa(serviceEmpresas.buscarPorCompanyCode(usuario.getEmpresa().getCompanyCode()));
		serviceVacantes.guardar(vacante);
		attributes.addFlashAttribute("msg", "Registro Guardado");
		return "redirect:/vacantes/indexPaginate";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante,
			RedirectAttributes attributes)
	{
		serviceVacantes.eliminar(idVacante);
		attributes.addFlashAttribute("msg","La vacante fue eliminada");
		return "redirect:/vacantes/indexPaginate";
	}
	
	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante , Model model)
	{
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		
		model.addAttribute("vacante", vacante);
		
		return "detalle";
	}
	
	@ModelAttribute
	public void setGenericos(Model model)
	{
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

	}
}
