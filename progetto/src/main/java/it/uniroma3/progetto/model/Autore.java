package it.uniroma3.progetto.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome","cognome"}))
public class Autore implements Comparable<Autore>{


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty 
	@NotNull
	private String nome;
	
	@NotEmpty
	@NotNull
	private String cognome;
	
	@NotEmpty
	@NotNull
	private String nazionalita;
	
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataNascita;
	
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dataMorte;
	
	//fetch di default
	@OneToMany(mappedBy="autore", cascade= {CascadeType.REMOVE})
	private List<Quadro> quadri;
 
	protected Autore() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Date getDataMorte() {
		return dataMorte;
	}

	public void setDataMorte(Date dataMorte) {
		this.dataMorte = dataMorte;
	}
	
	  public List<Quadro> getQuadri() {
		return quadri;
	}

	public void setQuadri(List<Quadro> quadri) {
		this.quadri = quadri;
	}
	
	@Override
	public int compareTo(Autore autore){
		int result;
		result = this.getNome().compareTo(autore.getNome());
		if (result == 0)
			result = this.getCognome().compareTo(autore.getCognome());
		return result;
	}
}
