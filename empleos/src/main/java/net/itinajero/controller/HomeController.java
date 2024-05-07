package net.itinajero.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.itinajero.model.Empresa;
import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriasService;
import net.itinajero.service.IEmpresasService;
import net.itinajero.service.IUsuariosService;
import net.itinajero.service.IVacanteService;

@Controller
public class HomeController {
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	// Inyectamos una instancia desde nuestro ApplicationContext
    @Autowired
    @Qualifier("vacantesServiceJpa")
	private IVacanteService serviceVacantes;
    
    @Autowired
    @Qualifier("usuariosServiceJpa")
   	private IUsuariosService serviceUsuarios;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IEmpresasService serviceEmpresas;
  
	@GetMapping("/")
	public String mostrarHome() {
		return "home";
	}
	
	/**
	 * Método que esta mapeado al botón Ingresar en el menú
	 * @param authentication
	 * @param session
	 * @return
	 */
	@GetMapping("/index")
	public String mostrarIndex(Authentication authentication, HttpSession session) {		
		
		// Como el usuario ya ingreso, ya podemos agregar a la session el objeto usuario.
		String username = authentication.getName();		
		
		for(GrantedAuthority rol: authentication.getAuthorities()) {
			System.out.println("ROL: " + rol.getAuthority());
		}
		
		if (session.getAttribute("usuario") == null){
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);
			usuario.setPassword(null);
			//System.out.println("Usuario: " + usuario);
			session.setAttribute("usuario", usuario);
		}
		
		return "redirect:/";
	}
	
	
	/**
	 * Método que muestra el formulario para que se registren nuevos usuarios.
	 * @param usuario
	 * @return
	 */
	@GetMapping("/signup")
	public String registrarse(Usuario usuario,Empresa empresa) {
		return "formRegistro";
	}
	
	/**
	 * Método que guarda en la base de datos el usuario registrado
	 * @param usuario
	 * @param attributes
	 * @return
	 */
	@PostMapping("/signup/Empresa")
	public String guardarRegistroEmpresa(Empresa empresa,RedirectAttributes attributes)
	{
		empresa.setCompanyCode(empresa.generarCompanyCode());
		serviceEmpresas.guardar(empresa);
		attributes.addFlashAttribute("msg", "Has sido registrado. Tu companyCode es: " + empresa.getCompanyCode());
		
		return "redirect:/";
	}
	@PostMapping("/signup/Usuario")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		// Recuperamos el password en texto plano
		String pwdPlano = usuario.getPassword();
		// Encriptamos el pwd BCryptPasswordEncoder
		String pwdEncriptado = passwordEncoder.encode(pwdPlano); 
		// Hacemos un set al atributo password (ya viene encriptado)
		usuario.setPassword(pwdEncriptado);	
		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor
		
		// Creamos el Perfil que le asignaremos al usuario nuevo
		Perfil perfil = new Perfil();
		if(usuario.getEmpresa().getCompanyCode()==null)
		{
			perfil.setId(3); // Perfil USUARIO
			usuario.agregar(perfil);
			usuario.setEmpresa(null);
			serviceUsuarios.guardar(usuario);
			
			attributes.addFlashAttribute("msg", "Has sido registrado. ¡Ahora puedes ingresar al sistema!");
		}
		else
		{
			Empresa empresa=serviceEmpresas.buscarPorCompanyCode(usuario.getEmpresa().getCompanyCode());
			if(Objects.nonNull(empresa))
			{
				perfil.setId(4); // Perfil USUARIO
				usuario.agregar(perfil);
				usuario.setEmpresa(empresa);
				serviceUsuarios.guardar(usuario);
				attributes.addFlashAttribute("msg", "Has sido registrado. ¡Ahora puedes ingresar al sistema!");
			}
			else
			{
				attributes.addFlashAttribute("msg", "No se ah encontrado en el sistema el Company Code asociado!");
			}
		}
		/**
		 * Guardamos el usuario en la base de datos. El Perfil se guarda automaticamente
		 
		serviceUsuarios.guardar(usuario);
				
		attributes.addFlashAttribute("msg", "Has sido registrado. ¡Ahora puedes ingresar al sistema!");
		*/
		return "redirect:/";
	}
	
	
	/**
	 * Método para realizar búsquedas desde el formulario de búsqueda del HomePage
	 * @param vacante
	 * @param model
	 * @return
	 */
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		
		/**
		 * La busqueda de vacantes desde el formulario debera de ser únicamente en Vacantes con estatus 
		 * "Aprobada". Entonces forzamos ese filtrado.
		 */
		vacante.setEstatus("Aprobada");
		
		// Personalizamos el tipo de busqueda...
		ExampleMatcher matcher  = ExampleMatcher.matching().
			// and descripcion like '%?%'
			withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = serviceVacantes.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}
	
	@GetMapping("/juegoOc")
	public String mostrarJuegoOc() {			
		return "juegoOc";
	}
	@GetMapping("/userExist")
	public ResponseEntity<String> verificarExistente(@RequestParam("name") String name, Model model) {
	    System.out.println("Nombre de usuario recibido: " + name);
	    
	    Usuario usuario = serviceUsuarios.buscarPorUsername(name);
	    if (usuario != null) 
	    {
	        return ResponseEntity.ok("Username no disponible");
	    } else 
	    {
	    	return ResponseEntity.ok(""); // Limpiar el atributo si el usuario no existe
	    }
	    
	}

	
	/**
	 * Metodo que muestra las historia de las empresas
	 * @return
	 */
	@GetMapping("/historiaEmpresa")
	public String mostrarhistoriaEmpresa(Model model) 
	{
		List<Empresa> lista = serviceEmpresas.buscarEmpresasnotnull();
		
	        if (lista.isEmpty()) 
	        {
	            return "histEmFall"; // Si esta vacia la lista, retorna "home" directamente
	        }
	        else
	        {
	        	model.addAttribute("empresas", lista);
	        	// Si contiene objetos la lista, se continúa y se muestra "historiaEmpresa"
	    	    return "historiaEmpresa";
	        }
	}
	
	/**
	 * Metodo que muestra la vista de la pagina de Acerca
	 * @return
	 */
	@GetMapping("/about")
	public String mostrarAcerca() {			
		return "acerca";
	}
	
	/**
	 * Metodo que muestra el creador Millan
	 * @return
	 */
	@GetMapping("/Millan")
	public String mostrarMillan() {
		return "millan";
	}
	
	/**
	 * Metodo que muestra el creador Tajonar
	 * @return
	 */
	@GetMapping("/Tajonar")
	public String mostrarTajonar() {
		return "tajonar";
	}
	/**
	 * Método que muestra el formulario de login personalizado.
	 * @return
	 */
	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}
	
	/**
	 * Método personalizado para cerrar la sesión del usuario
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}
	
	/**
     * Utileria para encriptar texto con el algorito BCrypt
     * @param texto
     * @return
     */
    @GetMapping("/bcrypt/{texto}")
    @ResponseBody
   	public String encriptar(@PathVariable("texto") String texto) {    	
   		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
   	}
	
    /**
	 * InitBinder para Strings si los detecta vacios en el Data Binding los settea a NULL
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
    
	/**
	 * Metodo que agrega al modelo datos genéricos para todo el controlador
	 * @param model
	 */
	@ModelAttribute
	public void setGenericos(Model model){
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("search", vacanteSearch);
		model.addAttribute("vacantes", serviceVacantes.buscarTodas());	
		model.addAttribute("categorias", serviceCategorias.buscarTodas());	
	}
		
}
