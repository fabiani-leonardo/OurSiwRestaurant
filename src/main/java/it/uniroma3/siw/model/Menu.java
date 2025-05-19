package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Menu {
	
	@OneToMany
	private List<VoceMenu> vociMenu;

	public List<VoceMenu> getVociMenu() {
		return vociMenu;
	}

	public void setVociMenu(List<VoceMenu> vociMenu) {
		this.vociMenu = vociMenu;
	}
	
	

}
