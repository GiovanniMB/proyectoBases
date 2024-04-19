package net.itinajero.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import net.itinajero.model.Agenda;
import net.itinajero.model.Solicitud;
import net.itinajero.model.Usuario;
import net.itinajero.service.IAgendaService;
import net.itinajero.service.ISolicitudesService;

@Controller
@RequestMapping("/agenda")
public class AgendaController 
{
	@Autowired
	private IAgendaService serviceAgenda;
	@Autowired
	private ISolicitudesService serviceSolicitudes;
	
	@GetMapping("/crearEntrevista/{idSolicitud}")
	public String agendar(Agenda agenda,@PathVariable("idSolicitud") Integer idSolicitud, Model model)
	{
		Solicitud solicitud = serviceSolicitudes.buscarPorId(idSolicitud);
		agenda.setSolicitud(solicitud);
		model.addAttribute("solicitud", solicitud);
		model.addAttribute("agenda", agenda);	
		System.out.println(agenda.toString());
		return"agenda/formAgenda";
	}
	@PostMapping("/save")
	public String guardar(Agenda agenda,HttpSession session, RedirectAttributes attributes)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		agenda.setUsuarioRH(usuario);
		System.out.println(agenda.toString());
		serviceAgenda.guardar(agenda);
		attributes.addFlashAttribute("msg", "Se guardo con exito la entrevista");
		return "redirect:/";
	}
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page,HttpSession session)
	{
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		Page<Agenda> lista = serviceAgenda.buscarPorCompanyCode(page, usuario.getEmpresa().getCompanyCode());
		model.addAttribute("agendas",lista);
		return "agenda/listAgenda";
	}
	
	@GetMapping("/contratar/{id}")
	public String contratar(@PathVariable("id") int idAgenda, RedirectAttributes attributes)
	{
		Agenda agenda = serviceAgenda.buscarPorid(idAgenda);
		serviceAgenda.eliminar(idAgenda);
		serviceSolicitudes.eliminar(agenda.getSolicitud().getId());
		attributes.addFlashAttribute("msg", "Contratacion existosa!");
		return "redirect:/agenda/indexPaginate";
	}
	@GetMapping("/cancelar/{id}")
	public String cancelar(@PathVariable("id") int idAgenda, RedirectAttributes attributes)
	{
		serviceAgenda.eliminar(idAgenda);
		attributes.addFlashAttribute("msg", "Cancelacion de entrevista existosa!");
		return "redirect:/agenda/indexPaginate";
	}
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) 
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

	}
}
