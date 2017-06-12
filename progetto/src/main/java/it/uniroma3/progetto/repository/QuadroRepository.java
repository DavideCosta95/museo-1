package it.uniroma3.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.progetto.model.Quadro;



public interface QuadroRepository extends CrudRepository<Quadro, Long> {

    List<Quadro> findByTitolo(String title);

    
}