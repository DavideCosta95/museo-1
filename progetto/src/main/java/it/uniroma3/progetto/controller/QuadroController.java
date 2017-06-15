package it.uniroma3.progetto.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.progetto.model.Autore;
import it.uniroma3.progetto.model.Quadro;
import it.uniroma3.progetto.service.AutoreService;
import it.uniroma3.progetto.service.QuadroService;


@Controller
public class QuadroController  {

	@Autowired
	private QuadroService quadroService; 

	@Autowired
	private AutoreService autoreService;

	@GetMapping("/quadro")
	public String showForm(Quadro quadro, Model model) {
		List<Autore> autori = (List<Autore>) autoreService.findAll(); 
		model.addAttribute("autori",autori);
		return "formQuadro";
	}


	@PostMapping("/quadro")
	public String checkQuadroInfo(@Valid @ModelAttribute Quadro quadro,
			BindingResult bindingResult, Model model,
			@RequestParam(value = "autoriEsistenti", required = false) Long autoriEsistenti) {
		if (bindingResult.hasErrors()) { 
			return "formQuadro";
		}
		else{
			try{
				Autore aut= autoreService.findById(autoriEsistenti);
				quadro.setAutore(aut);
				model.addAttribute(quadro);
				quadroService.add(quadro); 
			}catch(Exception e){
				List<Autore> autori = (List<Autore>) autoreService.findAll(); 
				model.addAttribute("autori",autori);
				return "formQuadro";
			}
		}
		return "confermaQuadro";
	}

}


