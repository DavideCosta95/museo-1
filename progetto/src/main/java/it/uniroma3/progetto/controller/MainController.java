package it.uniroma3.progetto.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import it.uniroma3.progetto.model.Autore;
import it.uniroma3.progetto.model.Quadro;
import it.uniroma3.progetto.service.AutoreService;
import it.uniroma3.progetto.service.QuadroService;

@Controller
public class MainController {

	@Autowired
	private QuadroService quadroService;
	@Autowired
	private AutoreService autoreService;
	
	
	@RequestMapping("/")
	public String main() {
		return "index";
	}


	@RequestMapping("/login")
	public String error() {
		return "formLogin";
	}
	
	
	@RequestMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "formLogin";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null){
			new SecurityContextLogoutHandler().logout(request,response,auth);
		}
		return "index";
	}
	
	@RequestMapping("/pannelloAmministratore")
	public String apriPannelloAmministratore(Model model){
		System.out.println("AAAAAAA");
			return "pannelloAmministratore";
	}

	@GetMapping(value="/lista")
	public String mostraQuadri(@ModelAttribute("tipo") String tipoLista, Model model) {
		if(tipoLista.equals("quadro")) {
			model.addAttribute("testo", "La nostra collezione");
			model.addAttribute("titolo", "Lista quadri");
			List<Quadro> quadri = (List<Quadro>) quadroService.findAll(); 
			model.addAttribute("quadri",quadri);
		}
		if(tipoLista.equals("autore")) {
			model.addAttribute("testo", "I nostri autori");
			model.addAttribute("titolo", "Lista autori");
			List<Autore> autori = (List<Autore>) autoreService.findAll(); 
			model.addAttribute("autori",autori);
		}
		return "lista";
	}
	
	@GetMapping(value = "/dettagliQuadro")
	public String dettagliQuadro(@ModelAttribute("id") Long id, Model model){
		Quadro quadro = quadroService.findById(id);
		model.addAttribute("quadro", quadro);
		model.addAttribute("testo", "Dettagli di:");
		model.addAttribute("titolo", quadro.getTitolo());
		model.addAttribute("action", "/");
		return "informazioniQuadro";
	}
	
	@GetMapping(value = "/dettagliAutore")
	public String dettagliAutore(@ModelAttribute("id") Long id, Model model){
		Autore autore = autoreService.findById(id);
		model.addAttribute("autore", autore);
		model.addAttribute("testo", "Dettagli di:");
		model.addAttribute("titolo", autore.getNome() + " " + autore.getCognome());
		model.addAttribute("action", "/");
		return "informazioniAutore";
	}
}
