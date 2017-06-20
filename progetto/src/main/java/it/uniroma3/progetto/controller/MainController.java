package it.uniroma3.progetto.controller;

import java.io.File;
import java.net.URL;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

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
		try{
		File file= new File(this.getClass().getResource("/static/img/1.png").getFile());
		System.out.println(file.getAbsolutePath());}
		catch(Exception e){
			System.out.println(e.toString());	
		}

		return "index";
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle(Exception ex) {
	    return "redirect:/404";
	}

	@RequestMapping("/404")
	public String NotFoudPage() {
	    return "404";
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

	@RequestMapping(value="/lista")
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
}
