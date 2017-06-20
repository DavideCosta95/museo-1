package it.uniroma3.progetto.controller;

import java.util.Collections;
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

	@GetMapping("/inserisciAutore")
	public String mostraFormAutore(Autore autore, Model model) {
		model.addAttribute("action", "/inserisciAutore");
		model.addAttribute("testo", "Nuovo autore");
		model.addAttribute("titolo", "Nuovo autore");
		model.addAttribute("testoBottone", "Inserisci");
		return "formAutore";
	}

	@PostMapping("/inserisciAutore")
	public String convalidaAutore(@Valid @ModelAttribute Autore autore, BindingResult bindingResult, Model model) {
		model.addAttribute("autore", autore);
		if (bindingResult.hasErrors()) {
			model.addAttribute("action", "/inserisciAutore");
			model.addAttribute("testo", "Nuovo autore");
			model.addAttribute("titolo", "Nuovo autore");
			model.addAttribute("testoBottone", "Inserisci");
			return "formAutore";
		}
		else {
			try{
				autoreService.add(autore);
				model.addAttribute("autore",autore);
				model.addAttribute("testo", "Autore inserito");
				model.addAttribute("titolo", "Autore inserito");
				model.addAttribute("testoBottone", "Inserisci");
				model.addAttribute("action",  "/pannelloAmministratore");
				return "informazioniAutore";
			}catch(Exception e) {
				model.addAttribute("action", "/inserisciAutore");
				model.addAttribute("testo", "Nuovo autore");
				model.addAttribute("titolo", "Nuovo autore");
				model.addAttribute("testoBottone", "Inserisci");
				return "formAutore";
			}
		}
	} 

	@GetMapping("/selezionaAutore")
	public String selezionaAutore(Model model){
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(autori.isEmpty()){
			model.addAttribute("nessunAutoreModificaAutore",true);
			return "pannelloAmministratore";
		}
		Collections.sort(autori);
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/modificaAutore");
		model.addAttribute("testo", "Modifica autore");
		model.addAttribute("titolo", "Modifica autore");
		model.addAttribute("testoBottone", "Modifica");
		return "selezionaAutore";
	}

	@PostMapping("/modificaAutore")
	public String modificaAutore(@RequestParam(value = "autoreSelezionato", required=true) Long autoreSelezionatoID, Model model){
		if(autoreSelezionatoID<0){
			model.addAttribute("erroreAutore", true);
			List<Autore> autori = (List<Autore>) autoreService.findAll();
			Collections.sort(autori);
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/modificaAutore");
			model.addAttribute("testo", "Seleziona autore");
			model.addAttribute("titolo", "Seleziona autore");
			model.addAttribute("testoBottone", "Seleziona");
			return "selezionaAutore";
		}
		Autore autore= autoreService.findById(autoreSelezionatoID);
		model.addAttribute("autore",autore);
		model.addAttribute("action", "/confermaModificaAutore");
		model.addAttribute("testo", "Modifica autore");
		model.addAttribute("titolo", "Modifica autore");
		model.addAttribute("testoBottone", "Modifica");
		return "formAutore";
	}
	
	@PostMapping("/confermaModificaAutore")
	public String confermaModifica(@Valid @ModelAttribute Autore autore,BindingResult bindingResult, Model model){
		if(bindingResult.hasErrors()) {
			model.addAttribute("autore",autore);
			model.addAttribute("action", "/confermaModificaAutore");
			model.addAttribute("testo", "Modifica autore");
			model.addAttribute("titolo", "Modifica autore");
			model.addAttribute("testoBottone", "Modifica");
			return "formAutore";
		}
		else
		{
			try{
				model.addAttribute("autore",autore);
				autoreService.add(autore);
				model.addAttribute("titolo", "Autore modificato");
				model.addAttribute("testo", "Autore modificato");
				model.addAttribute("action",  "/pannelloAmministratore");
				model.addAttribute("autorizzato",  "autorizzato");
				return "informazioniAutore";
			}catch(Exception e){
				model.addAttribute("autore",autore);
				return "modificaAutoreForm";
			}
		}
	}
	
	@GetMapping("/eliminaAutore")
	public String mostraFormEliminaAutore(Model model){
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(autori.isEmpty()){
			model.addAttribute("nessunAutoreEliminaAutore",true);
			return "pannelloAmministratore";
		}
		Collections.sort(autori);
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/eliminaAutore");
		model.addAttribute("testoBottone", "Elimina");
		model.addAttribute("testo", "Elimina autore");
		model.addAttribute("titolo", "Elimina autore");
		return "selezionaAutore";
	}
	
	@PostMapping("/eliminaAutore")
	public String eliminaAutore(@RequestParam(value = "autoreSelezionato", required=true) Long autoreSelezionatoID, Model model){
		Autore autore = autoreService.findById(autoreSelezionatoID);
		autoreService.delete(autore);
		model.addAttribute("autore", autore);
		model.addAttribute("titolo", "Autore eliminato");
		model.addAttribute("testo", "Autore eliminato");
		model.addAttribute("action",  "/pannelloAmministratore");
		model.addAttribute("autorizzato",  "autorizzato");
		return "informazioniAutore";
	}
}
