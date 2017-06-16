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
import it.uniroma3.progetto.service.AutoreService;

@Controller
public class AutoreController {

	@Autowired 
	private AutoreService autoreService;


	@GetMapping("/autore")
	public String showForm(Autore autore) {
		return "formAutore";
	}

	@PostMapping("/autore")
	public String checkAutoreInfo(@Valid @ModelAttribute Autore autore, 
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "formAutore";
		}
		else {
			model.addAttribute(autore);
			try{
				autoreService.add(autore); 
			}catch(Exception e){
				return "formAutore";
			}
		}
		return "confermaAutore";
	} 

	@GetMapping("/selezionaAutore")
	public String selezionaAutore(Model model){
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(autori.isEmpty())
			return "pannelloAmministratore";
		model.addAttribute("autori",autori);
		return "selezionaAutore";
	}

	@PostMapping("/modificaAutore")
	public String modificaAutore(@RequestParam(value = "autoriEsistenti") Long autoreSelezionatoID, Model model){
		Autore autore= autoreService.findById(autoreSelezionatoID);
		model.addAttribute("autoreSelezionato",autore);
		return "modificaAutoreForm";
	}

	@PostMapping("/confermaModificaAutore")
	public String confermaModifica(@Valid @ModelAttribute Autore autore,Model model,BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "modificaAutoreForm";
		}
		else
		{
			try{
				autoreService.add(autore);
				return "confermaAutore";
			}catch(Exception e){
				model.addAttribute("autoreSelezionato",autore);
				return "modificaAutoreForm";
			}
		}
	}

}
