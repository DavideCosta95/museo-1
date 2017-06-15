package it.uniroma3.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.progetto.model.Autore;


public interface AutoreRepository extends CrudRepository<Autore, Long> {

    List<Autore> findByNome(String nome);
    Autore findByCognome(String nome);
//    Autore merge(Autore autore);

}
