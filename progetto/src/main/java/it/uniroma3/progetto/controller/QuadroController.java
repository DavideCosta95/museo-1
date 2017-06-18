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

	@GetMapping("/inserisciQuadro")
	public String showForm(Quadro quadro, Model model) {
		List<Autore> autori=(List<Autore>) autoreService.findAll();

		if(autori.isEmpty()){
			model.addAttribute("nessunAutoreInserisciQuadro",true);
			return "pannelloAmministratore";
		}else{
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/inserisciQuadro");
			model.addAttribute("testo", "Inserisci un nuovo quadro:");
			model.addAttribute("titolo", "Nuovo quadro");
			model.addAttribute("testoBottone", "Inserisci");
			return "formQuadro";
		}
	}

	@PostMapping("/inserisciQuadro")
	public String checkQuadroInfo(@Valid @ModelAttribute Quadro quadro,
			BindingResult bindingResult, Model model,
			@RequestParam(value = "autore") Long autoreID) {
		if (bindingResult.hasErrors() || autoreID<0) { 
			List<Autore> autori = (List<Autore>) autoreService.findAll(); 
			model.addAttribute("autori",autori);
			model.addAttribute("quadro", quadro);
			model.addAttribute("autore", autoreID);
			model.addAttribute("action", "/inserisciQuadro");
			model.addAttribute("testo", "Inserisci un nuovo quadro:");
			model.addAttribute("titolo", "Nuovo quadro");
			model.addAttribute("testoBottone", "Inserisci");
				if(autoreID<0)
					model.addAttribute("erroreAutore", true);
			return "formQuadro";
		}
		else{
			try{
				Autore autore = autoreService.findById(autoreID);
				quadro.setAutore(autore);
				model.addAttribute("quadro",quadro);
				quadroService.add(quadro); 
			}catch(Exception e){
				List<Autore> autori = (List<Autore>) autoreService.findAll(); 
				model.addAttribute("autori",autori);
				model.addAttribute("quadro", quadro);
				model.addAttribute("autore", autoreID);
				model.addAttribute("action", "/inserisciQuadro");
				model.addAttribute("testo", "Inserisci un nuovo quadro:");
				model.addAttribute("titolo", "Nuovo quadro");
				model.addAttribute("testoBottone", "Inserisci");
				return "formQuadro";
			}
		}
		return "confermaQuadro";
	}


	@GetMapping("/modificaQuadro")
	public String mostraFormModificaQuadro(Model model){
		List<Quadro> quadri=(List<Quadro>) quadroService.findAll();
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(quadri.isEmpty()){
			model.addAttribute("nessunQuadroModificaQuadro",true);
			return "pannelloAmministratore";
		}
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/modificaQuadro");
		model.addAttribute("testo", "Seleziona quadro:");
		model.addAttribute("titolo", "Seleziona quadro");
		model.addAttribute("testoBottone", "Seleziona");
		return "selezionaQuadro";
	}

	@PostMapping("/modificaQuadro")
	public String modificaQuadro(@RequestParam(value = "autoreSelezionato") Long autoreSelezionatoID,
			@RequestParam(value = "titoloQuadro") String titoloQuadroSelezionato, Model model){
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		
		if(autoreSelezionatoID<0) {
			model.addAttribute("action", "/modificaQuadro");
			model.addAttribute("testo", "Seleziona quadro:");
			model.addAttribute("titolo", "Seleziona quadro");
			model.addAttribute("testoBottone", "Seleziona");
			return "selezionaQuadro";
		}
		
		Autore autoreSelezionato = autoreService.findById(autoreSelezionatoID);
		List<Quadro> quadriAutoreSelezionato = autoreSelezionato.getQuadri();
		Quadro quadroTrovato = null;

		for(Quadro q : quadriAutoreSelezionato)
			if(q.getTitolo().equals(titoloQuadroSelezionato))
				quadroTrovato = q;

		model.addAttribute("autori",autori);

		if (quadroTrovato != null) {
			model.addAttribute("quadro", quadroTrovato);
			model.addAttribute("action", "/confermaModificaQuadro");
			model.addAttribute("testo", "Modifica quadro:");
			model.addAttribute("titolo", "Modifica quadro");
			model.addAttribute("testoBottone", "Modifica");
			return "formQuadro";
		} else {
			model.addAttribute("action", "/modificaQuadro");
			model.addAttribute("testo", "Seleziona quadro:");
			model.addAttribute("titolo", "Seleziona quadro");
			model.addAttribute("testoBottone", "Seleziona");
			return "selezionaQuadro";
		}
	}

	@PostMapping("/confermaModificaQuadro")
	public String confermaModifica(@Valid @ModelAttribute Quadro quadro,BindingResult bindingResult, Model model,
			@RequestParam(value = "id", required = true) Long autore){

		if(bindingResult.hasErrors()) {
			List<Autore> autori= (List<Autore>) autoreService.findAll();
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/confermaModificaQuadro");
			model.addAttribute("testo", "Modifica quadro:");
			model.addAttribute("titolo", "Modifica quadro");
			model.addAttribute("testoBottone", "Modifica");
			return "formQuadro";
		}
		else{
			Autore a = autoreService.findById(autore);
			quadro.setAutore(a);
			model.addAttribute("quadro",quadro);
			try{
				quadroService.add(quadro);	
				return "confermaQuadro";
			}catch(Exception e){
				model.addAttribute("action", "/confermaModificaQuadro");
				model.addAttribute("testo", "Modifica quadro:");
				model.addAttribute("titolo", "Modifica quadro");
				model.addAttribute("testoBottone", "Modifica");
				return "formQuadro";
			}
		}
	}
	
	@GetMapping("/eliminaQuadro")
	public String mostraFormEliminaQuadro(Model model){
		List<Quadro> quadri=(List<Quadro>) quadroService.findAll();
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(quadri.isEmpty()){
			model.addAttribute("nessunQuadroEliminaQuadro",true);
			return "pannelloAmministratore";
		}
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/eliminaQuadro");
		model.addAttribute("testo", "Elimina quadro:");
		model.addAttribute("titolo", "Elimina quadro");
		model.addAttribute("testoBottone", "Elimina");
		return "selezionaQuadro";
	}
	
	@PostMapping("/eliminaQuadro")
	public String eliminaQuadro(@RequestParam(value = "autoreSelezionato", required = true) Long autoreSelezionatoID,
			@RequestParam(value = "titoloQuadro") String titoloQuadroSelezionato, Model model){
		Autore autoreSelezionato = autoreService.findById(autoreSelezionatoID);
		List<Quadro> quadriAutoreSelezionato = autoreSelezionato.getQuadri();
		Quadro quadroTrovato=null;

		for(Quadro q : quadriAutoreSelezionato)
			if(q.getTitolo().equals(titoloQuadroSelezionato))
				quadroTrovato=q;
		try{
			quadroService.delete(quadroTrovato);
			model.addAttribute("quadro", quadroTrovato);
			model.addAttribute("titolo", "Quadro eliminato");
			model.addAttribute("testo", "Hai eliminato il quadro:");
			return "confermaOperazioneQuadro";
		}
		catch(Exception e){
			List<Autore> autori= (List<Autore>) autoreService.findAll();
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/eliminaQuadro");
			model.addAttribute("testo", "Elimina quadro:");
			model.addAttribute("titolo", "Elimina quadro");
			model.addAttribute("testoBottone", "Elimina");
			return "selezionaQuadro";
		}
	}	
}





