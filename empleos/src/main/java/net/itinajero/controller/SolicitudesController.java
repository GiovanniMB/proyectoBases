package net.itinajero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import net.itinajero.model.Solicitud;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.ISolicitudesService;
import net.itinajero.service.IUsuariosService;
import net.itinajero.service.IVacanteService;
import net.itinajero.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController 
{
	@Value("${empleosapp.ruta.cv}")
	private String rutaCv;
	@Autowired
	private IVacanteService serviceVacantes;
	@Autowired
	private IUsuariosService serviceUsuario;
	@Autowired
	private ISolicitudesService serviceSolicitudes;
	
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page,HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Page<Solicitud> lista = serviceSolicitudes.buscarPorCompanyCodeAndNotA(page, usuario.getEmpresa().getCompanyCode());
		model.addAttribute("solicitudes",lista);
		return "solicitudes/listSolicitudes";
	}
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud,@PathVariable Integer idVacante, Model model)
	{
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante",vacante);
		return "solicitudes/formSolicitud";
	}
	@PostMapping("/save")
	public String guardar(Solicitud solicitud,BindingResult result,
			@RequestParam("archivoCV") MultipartFile multipart,
			Authentication authentication, RedirectAttributes attributes)
	{
		String username = authentication.getName();
		Usuario usuario = serviceUsuario.buscarPorUsername(username);
		if(serviceSolicitudes.buscarExistente(solicitud.getVacante().getId(), usuario.getId())>0)
		{
			attributes.addFlashAttribute("msg1","Gracias por el interes, ya has mandado anteriormente tu CV!");
			return "redirect:/";
		}
		else
		{
			if(result.hasErrors())
			{
				System.out.println("Existieron errores");
				return "solicitudes/formSolicitud";
			}
			if(!multipart.isEmpty())
			{
				String nombreArchivo = Utileria.guardarArchivo(multipart, rutaCv);
				if(nombreArchivo!=null)
				{
					solicitud.setArchivo(nombreArchivo);
				}
			}
			
			solicitud.setUsuario(usuario);
			serviceSolicitudes.guardar(solicitud);
			attributes.addFlashAttribute("msg","Gracias por enviar tu CV!");
			return "redirect:/";
		}
	}
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes)
	{
		serviceSolicitudes.eliminar(idSolicitud);
		attributes.addFlashAttribute("msg", "La solicitud fue eliminada");
		return "redirect:/solicitudes/indexPaginate";
	}
}
