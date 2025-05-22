package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VoceMenu {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(length= 250)
	private String descrizione;
	@Column(nullable = false)
	private int prezzo;
	
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
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(nome, prezzo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoceMenu other = (VoceMenu) obj;
		return Objects.equals(nome, other.nome) && prezzo == other.prezzo;
	}
	

	
}

