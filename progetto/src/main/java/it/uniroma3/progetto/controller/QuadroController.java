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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.progetto.model.Autore;
import it.uniroma3.progetto.model.Quadro;
import it.uniroma3.progetto.service.AutoreService;
import it.uniroma3.progetto.service.QuadroService;
import it.uniroma3.progetto.service.UploaderImmagine;


@Controller
public class QuadroController  {

	@Autowired
	private QuadroService quadroService; 

	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private UploaderImmagine uploader;

	@GetMapping("/inserisciQuadro")
	public String mostraFormQuadro(Quadro quadro, Model model) {
		List<Autore> autori=(List<Autore>) autoreService.findAll();

		if(autori.isEmpty()){
			model.addAttribute("nessunAutoreInserisciQuadro",true);
			return "pannelloAmministratore";
		}else{			
			Collections.sort(autori);
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/inserisciQuadro");
			model.addAttribute("testo", "Nuovo quadro");
			model.addAttribute("titolo", "Nuovo quadro");
			model.addAttribute("testoBottone", "Inserisci");
			return "formQuadro";
		}
	}

	@PostMapping("/inserisciQuadro")
	public String convalidaQuadro(@Valid @ModelAttribute Quadro quadro, BindingResult bindingResult, Model model,
			@RequestParam(value = "autore") Long autoreID,
			@RequestParam(value = "immagineQuadro", required = false) MultipartFile immagine) {
		if (bindingResult.hasErrors() || autoreID<0) {
			List<Autore> autori = (List<Autore>) autoreService.findAll();
			Collections.sort(autori);
			model.addAttribute("autori",autori);
			model.addAttribute("quadro", quadro);
			model.addAttribute("autore", autoreID);
			model.addAttribute("action", "/inserisciQuadro");
			model.addAttribute("testo", "Nuovo quadro");
			model.addAttribute("titolo", "Nuovo quadro");
			model.addAttribute("testoBottone", "Inserisci");
				if(autoreID<0)
					model.addAttribute("erroreAutore", true);
			return "formQuadro";
		}
		else{
			try{
				if (!(immagine == null || immagine.isEmpty())){
					uploader.creaCartellaImmagini();
					uploader.creaFileImmagine(immagine.getOriginalFilename(), immagine);
					quadro.setImmagine(immagine.getOriginalFilename());
				}
				Autore autore = autoreService.findById(autoreID);
				quadro.setAutore(autore);
				model.addAttribute("quadro",quadro);
				quadroService.add(quadro);
				model.addAttribute("testo", "Quadro inserito");
				model.addAttribute("titolo", "Quadro inserito");
				model.addAttribute("action", "/pannelloAmministratore");
				return "informazioniQuadro";
			}catch(Exception e){
				List<Autore> autori = (List<Autore>) autoreService.findAll();
				Collections.sort(autori);
				model.addAttribute("autori",autori);
				model.addAttribute("quadro", quadro);
				model.addAttribute("autore", autoreID);
				model.addAttribute("action", "/inserisciQuadro");
				model.addAttribute("testo", "Nuovo quadro");
				model.addAttribute("titolo", "Nuovo quadro");
				model.addAttribute("testoBottone", "Inserisci");
				return "formQuadro";
			}
		}
	}


	@GetMapping("/modificaQuadro")
	public String mostraFormModificaQuadro(Model model){
		List<Quadro> quadri = (List<Quadro>) quadroService.findAll();
		List<Autore> autori = (List<Autore>) autoreService.findAll();
		if (quadri.isEmpty()) {
			model.addAttribute("nessunQuadroModificaQuadro",true);
			return "pannelloAmministratore";
		}
		Collections.sort(autori);
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/modificaQuadro");
		model.addAttribute("testo", "Seleziona quadro");
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
			model.addAttribute("testo", "Seleziona quadro");
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
		Collections.sort(autori);
		model.addAttribute("autori",autori);

		if (quadroTrovato != null) {
			model.addAttribute("quadro", quadroTrovato);
			model.addAttribute("action", "/confermaModificaQuadro");
			model.addAttribute("testo", "Modifica quadro");
			model.addAttribute("titolo", "Modifica quadro");
			model.addAttribute("testoBottone", "Modifica");
			return "formQuadro";
		} else {
			model.addAttribute("action", "/modificaQuadro");
			model.addAttribute("testo", "Seleziona quadro");
			model.addAttribute("titolo", "Seleziona quadro");
			model.addAttribute("testoBottone", "Seleziona");
			return "selezionaQuadro";
		}
	}

	@PostMapping("/confermaModificaQuadro")
	public String confermaModifica(@Valid @ModelAttribute Quadro quadro,BindingResult bindingResult, Model model,
			@RequestParam(value = "autore", required = true) Long autore){

		if(bindingResult.hasErrors()) {
			List<Autore> autori= (List<Autore>) autoreService.findAll();
			Collections.sort(autori);
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/confermaModificaQuadro");
			model.addAttribute("testo", "Modifica quadro");
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
				model.addAttribute("testo", "Quadro modificato");
				model.addAttribute("titolo", "Quadro modificato");
				model.addAttribute("action", "/pannelloAmministratore");
				return "informazioniQuadro";
			}catch(Exception e){
				model.addAttribute("action", "/confermaModificaQuadro");
				model.addAttribute("testo", "Modifica quadro");
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
		Collections.sort(autori);
		model.addAttribute("autori",autori);
		model.addAttribute("action", "/eliminaQuadro");
		model.addAttribute("testo", "Elimina quadro");
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
			model.addAttribute("testo", "Quadro eliminato");
			model.addAttribute("action", "/pannelloAmministratore");
			return "informazioniQuadro";
		}
		catch(Exception e){
			List<Autore> autori= (List<Autore>) autoreService.findAll();
			Collections.sort(autori);
			model.addAttribute("autori",autori);
			model.addAttribute("action", "/eliminaQuadro");
			model.addAttribute("testo", "Elimina quadro");
			model.addAttribute("titolo", "Elimina quadro");
			model.addAttribute("testoBottone", "Elimina");
			return "selezionaQuadro";
		}
	}
	
	@GetMapping(value = "/dettagliQuadro")
	public String dettagliQuadro(@ModelAttribute("id") Long id, Model model){
		Quadro quadro = quadroService.findById(id);
		model.addAttribute("quadro", quadro);
		model.addAttribute("testo", "Dettagli di " + quadro.getTitolo());
		model.addAttribute("titolo", quadro.getTitolo());
		model.addAttribute("action", "/");
		return "informazioniQuadro";
	}
}





