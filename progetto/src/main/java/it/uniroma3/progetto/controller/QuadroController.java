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
		List<Autore> autori=(List<Autore>) autoreService.findAll();

		if(autori.isEmpty()){
			model.addAttribute("nessunAutore1",true);
			return "pannelloAmministratore";
		}else{
			model.addAttribute("autori",autori);
			return "formQuadro";
		}
	}

	@PostMapping("/quadro")
	public String checkQuadroInfo(@Valid @ModelAttribute Quadro quadro,
			BindingResult bindingResult, Model model,
			@RequestParam(value = "autore") Long autoreID) {
		if (bindingResult.hasErrors()) { 
			List<Autore> autori = (List<Autore>) autoreService.findAll(); 
			model.addAttribute("autori",autori);
			model.addAttribute("quadro", quadro);
			model.addAttribute("autore", autoreID);
			return "formQuadro";
		}
		else if(autoreID<0){
			List<Autore> autori = (List<Autore>) autoreService.findAll();
			model.addAttribute("autori",autori);
			model.addAttribute("quadro", quadro);
			model.addAttribute("autore", autoreID);
			model.addAttribute("erroreAutore", true);
			return "formQuadro";
		}
		else{
			try{
				Autore aut= autoreService.findById(autoreID);
				quadro.setAutore(aut);
				model.addAttribute("quadro",quadro);
				quadroService.add(quadro); 
			}catch(Exception e){
				List<Autore> autori = (List<Autore>) autoreService.findAll(); 
				model.addAttribute("autori",autori);
				return "formQuadro";
			}
		}
		return "confermaQuadro";
	}


	@GetMapping("/selezionaQuadro")
	public String selezionaQuadro(Model model){
		List<Quadro> quadri=(List<Quadro>) quadroService.findAll();
		List<Autore> autori= (List<Autore>) autoreService.findAll();
		if(quadri.isEmpty()){
			model.addAttribute("nessunQuadro1",true);
			return "pannelloAmministratore";
		}
		model.addAttribute("autori",autori);
		return "selezionaQuadro";
	}

	@PostMapping("/modificaQuadro")
	public String modificaQuadro(@RequestParam(value = "autoreSelezionato") Long autoreSelezionatoID,
			@RequestParam(value = "titoloQuadro") String titoloQuadroSelezionato, Model model){

		List<Autore> autori = (List<Autore>) autoreService.findAll(); 
		Autore autoreSelezionato= autoreService.findById(autoreSelezionatoID);
		List<Quadro> quadriAutoreSelezionato =autoreSelezionato.getQuadri();
		Quadro quadroTrovato=null;

		for(Quadro q:quadriAutoreSelezionato){
			if(q.getTitolo().equals(titoloQuadroSelezionato)){
				quadroTrovato=q;
			}
		}

		model.addAttribute("autori",autori);

		if(quadroTrovato!=null){
			model.addAttribute("quadro",quadroTrovato);
			return "modificaQuadroForm";
		}else{
			return "selezionaQuadro";
		}
	}

	@PostMapping("/confermaModificaQuadro")
	public String confermaModifica(@Valid @ModelAttribute Quadro quadro,BindingResult bindingResult, Model model,
			@RequestParam(value = "id", required = false) Long autore){

		if(bindingResult.hasErrors()){
			List<Autore> autori= (List<Autore>) autoreService.findAll();
			model.addAttribute("autori",autori);
			return "modificaQuadroForm";
		}
		else{
			Autore a = autoreService.findById(autore);
			quadro.setAutore(a);
			model.addAttribute("quadro",quadro);
			try{
				quadroService.add(quadro);	
				return "confermaQuadro";
			}catch(Exception e){
				return "modificaQuadroForm";
			}
		}
	}

}





