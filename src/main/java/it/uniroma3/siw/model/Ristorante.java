package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Ristorante {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private  String indirizzo;
	private  Integer numeroTelefono;
	private  String immagine;
	@OneToOne
	private Menu menu;
	@OneToMany
	private List<Recensione> recensioni;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public Integer getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(Integer numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public String getImmagine() {
		return immagine;
	}
	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Recensione> getRecensioni() {
		return recensioni;
	}
	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}
	@Override
	public int hashCode() {
		return Objects.hash(indirizzo, nome);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ristorante other = (Ristorante) obj;
		return Objects.equals(indirizzo, other.indirizzo) && Objects.equals(nome, other.nome);
	}
	
	
	
	
	
	
	

}
