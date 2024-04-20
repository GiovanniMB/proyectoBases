package net.itinajero.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import net.itinajero.model.Colonia;
import net.itinajero.model.Empresa;
import net.itinajero.model.HistoriaEmpresa;
import net.itinajero.model.Municipio;
import net.itinajero.model.Socio;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.IAgendaService;
import net.itinajero.service.IColoniasService;
import net.itinajero.service.IEmpresasService;
import net.itinajero.service.IEstadosService;
import net.itinajero.service.IMunicipiosService;
import net.itinajero.service.ISolicitudesService;
import net.itinajero.service.IUsuariosService;
import net.itinajero.service.IVacanteService;
import net.itinajero.util.Utileria;

@Controller
@RequestMapping("/empresas")
public class EmpresasController 
{
	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;
	@Autowired
	private IEmpresasService serviceEmpresas;
	
	@Autowired
	@Qualifier("estadosServiceJpa")
	private IEstadosService serviceEstado;
	
	@Autowired
	@Qualifier("municipiosServiceJpa")
	private IMunicipiosService serviceMunicipio;
	
	@Autowired
	@Qualifier("coloniasServiceJpa")
	private IColoniasService serviceColonia;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	@Autowired
	private IAgendaService serviceAgenda;
	@Autowired
	private IVacanteService serviceVacantes;
	@Autowired
	private ISolicitudesService serviceSolicitudes;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model)
	{
		List<Empresa> lista = serviceEmpresas.buscarTodos();
		model.addAttribute("empresas", lista);
		return "empresas/listEmpresas";
	}
	
	@GetMapping("/update")
	public String Registrar(Empresa empresa, Model model,HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		empresa = serviceEmpresas.buscarPorCompanyCode(usuario.getEmpresa().getCompanyCode());
		// Asegúrate de que hisEmpresa no sea nulo y tenga socios inicializados
		if (empresa.getHisEmpresa() == null)
		{
		    empresa.setHisEmpresa(new HistoriaEmpresa());
		    empresa.getHisEmpresa().setSocios(new ArrayList<>());
		    model.addAttribute("empresa",empresa);
			return "empresas/formEmpresa";
		}
		model.addAttribute("empresa",empresa);
		return"empresas/datos";
	}
	
	@PostMapping("/save")
	public String guardar(Empresa empresa,@RequestParam("socios") String sociosJson, BindingResult result,
	        RedirectAttributes attributes,
	        @RequestParam("archivoImagen") MultipartFile multiPart,@RequestParam("archivoLogo") MultipartFile multiPart2) 
	{
	    if (result.hasErrors()) 
	    {
	        for (ObjectError error : result.getAllErrors()) 
	        {
	            System.out.println("Ocurrió un error: " + error.getDefaultMessage());
	        }
	        return "empresas/formEmpresa";
	    }
	    if (!multiPart.isEmpty())
	    {
	        String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
	        if (nombreImagen != null) 
	        {
	            // La imagen se subió correctamente
	            // Procesamos la variable nombreImagen
	            empresa.getHisEmpresa().setImagen(nombreImagen);
	        }
	    }
	    if (!multiPart2.isEmpty())
	    {
	        String nombreImagen2 = Utileria.guardarArchivo(multiPart2, ruta);
	        if (nombreImagen2 != null) 
	        {
	            // La imagen se subió correctamente
	            // Procesamos la variable nombreImagen
	            empresa.setImagen(nombreImagen2);
	        }
	    }
	    try {
	        // Deserializar la cadena JSON en una lista de nombres de socios
	        ObjectMapper objectMapper = new ObjectMapper();
	        List<String> nombresSocios = objectMapper.readValue(sociosJson, new TypeReference<List<String>>() {});

	        // Crear la lista de socios
	        List<Socio> socios = new ArrayList<>();
	        for (String nombreSocio : nombresSocios) 
	        {
	            Socio socio = new Socio();
	            socio.setNombre(nombreSocio);
	            System.out.println(socio);
	            // Agregar otros atributos si es necesario
	            socios.add(socio);
	        }

	    
	        // Asignar la lista de socios a la empresa
	        empresa.getHisEmpresa().setSocios(socios);
	    
	    System.out.println("socios"+ empresa.getHisEmpresa().getSocios().toString());
	    serviceEmpresas.guardar(empresa);
	    attributes.addFlashAttribute("msg", "Registro Guardado");
	    return "redirect:/";
	    } catch (IOException e) 
	    {
	        // Manejar la excepción si ocurre un problema al deserializar la cadena JSON
	        e.printStackTrace();
	        attributes.addFlashAttribute("msg", "Error en los nombres de soscios");
	        // Devolver una vista de error o redireccionar a una página de error
	        return "redirect:/";
	    }
	}
	@GetMapping("/delete/{companyCode}")
	@Transactional
	public String eliminar(@PathVariable("companyCode") String companyCode, RedirectAttributes attributes)
	{
		
		Usuario usuario = serviceUsuarios.buscarPorCompanyCode(companyCode);
		List<Vacante> list =serviceVacantes.buscarTodasPorCompanyCode(companyCode);
		for(Vacante vacante : list)
		{
			serviceSolicitudes.eliminarPorIdVacante(vacante.getId());
		}
		serviceVacantes.eliminarPorCompanyCode(companyCode);
		serviceAgenda.eliminarPorIdRH(usuario.getId());
		serviceUsuarios.eliminar(usuario.getId());
		serviceEmpresas.eliminar(companyCode);
		attributes.addFlashAttribute("msg", "La empresa fue eliminada!.");
		return "redirect:/empresas/index";
	}
	@GetMapping("/estado")
	public String ajaxEstados(@RequestParam("estadoId") Integer idEstado, Model model)
	{
		List<Municipio> municipios = serviceMunicipio.buscarPorIdEstado(idEstado);
		model.addAttribute("municipios",municipios);
		return "empresas/formEmpresa :: municipios";
	}
	@GetMapping("/municipio")
	public String ajaxMunicipio(@RequestParam("municipioId") Integer idMunicipio, Model model)
	{
		List<Colonia> colonias = serviceColonia.buscarPorIdMunicipio(idMunicipio);
		model.addAttribute("colonias", colonias);
		return "empresas/formEmpresa :: colonias";
	}
	@ModelAttribute
	public void setGenericos(Model model)
	{
		model.addAttribute("estados", serviceEstado.buscarTodos());
	}
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

	}
}