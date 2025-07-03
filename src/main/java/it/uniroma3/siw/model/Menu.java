package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Menu {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)// orphanRemoval serve a eliminare le VoceMenu rimosse dalla lista anche dal database.
	private List<MenuLine> menuLines;

	

	public List<MenuLine> getMenuLines() {
		return menuLines;
	}

	public void setMenuLines(List<MenuLine> menuLines) {
		this.menuLines = menuLines;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	

}
