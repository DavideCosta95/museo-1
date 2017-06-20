package it.uniroma3.progetto.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
			return "pannelloAmministratore";
	}

	@RequestMapping("/lista")
	public String mostraQuadri(@ModelAttribute("tipo") String tipoLista, Model model) {
		if(tipoLista.equals("quadro")) {
			model.addAttribute("testo", "La nostra collezione");
			model.addAttribute("titolo", "Lista quadri");
			List<Quadro> quadri = (List<Quadro>) quadroService.findAll();
			Collections.sort(quadri);
			model.addAttribute("quadri",quadri);
		}
		if(tipoLista.equals("autore")) {
			model.addAttribute("testo", "I nostri autori");
			model.addAttribute("titolo", "Lista autori");
			List<Autore> autori = (List<Autore>) autoreService.findAll();
			Collections.sort(autori);
			model.addAttribute("autori",autori);
		}
		return "lista";
	}
	
	@RequestMapping("/chiSiamo")
	public String mostraChiSiamo(Model model){
		return "chiSiamo";
	}
	@RequestMapping("/doveSiamo")
	public String mostraDoveSiamo(Model model){
		return "doveSiamo";
	}
	@RequestMapping("/contatti")
	public String mostraContatti(Model model){
		return "contatti";
	}
}
