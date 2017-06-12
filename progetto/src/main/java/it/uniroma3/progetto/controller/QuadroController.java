package it.uniroma3.progetto.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.progetto.model.Quadro;
import it.uniroma3.progetto.service.QuadroService;


@Controller
public class QuadroController  {
	
    @Autowired
    private QuadroService quadroservice; 
    
//    @Autowired
//    private AutoreService autoreService;

    @GetMapping("/quadro")
    public String showForm(Quadro quadro) {
        return "formQuadro";
    }
  
//    public String listaAutori(@Valid @ModelAttribute Autore autore, Model model) {
//    	if(autore.getId() != null){
//    		return "formAutore" ;
//    	}
//    		else{
//    			List<Autore> autori = (List<Autore>) autoreService.findAll();
//    			model.addAttribute(autori);
//    		}
//    	return "autori";
//    }

    @PostMapping("/quadro")
    public String checkQuadroInfo(@Valid @ModelAttribute Quadro quadro, 
    									BindingResult bindingResult, Model model) {
    	
        if (bindingResult.hasErrors()) {
            return "formQuadro";
        }
        else {
        	model.addAttribute(quadro);
        	quadroservice.add(quadro); 
        }
        return "confermaQuadro";
    }

}

