package it.uniroma3.progetto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.progetto.model.Quadro;

import it.uniroma3.progetto.service.QuadroService;

// controller to access the login page
@Controller
public class MainController {

	@Autowired
	private QuadroService quadroService;
	
	
	@RequestMapping("/")
	public String main() {
		return "index";
	}


	// Login form
	@PostMapping("/loginAmm")
	public String pannelloAmministratore() {
		return "pannelloAmministratore";
	}
	
	@GetMapping("/loginAmm")
	public String error() {
		return "login";
	}
	
	@GetMapping("/login")
	public String login() {
		return "pannelloAmministratore";
	}
	

	// Login form with error
	@RequestMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	//Lista di quadri
	@GetMapping(value="/listaQuadri")
	public String showForm(Model model){
		List<Quadro> quadri = (List<Quadro>) quadroService.findAll(); 
		model.addAttribute("quadri",quadri);
		return "listaQuadri";
	}

	@GetMapping(value = "/dettagli")
	public String dettagliQuadro(@ModelAttribute("id") Long id, Model model){
		Quadro quadro = quadroService.findbyId(id);
		model.addAttribute(quadro);
		return "dettagliQuadro";
	}
	

}
