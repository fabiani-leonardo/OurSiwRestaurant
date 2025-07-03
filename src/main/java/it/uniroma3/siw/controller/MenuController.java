package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.service.MenuLineService;
import it.uniroma3.siw.service.MenuService;

@Controller
public class MenuController {
	@Autowired 
	private MenuService menuService;
	@Autowired 
	private MenuLineService menuLineService;
	
	
	//visualizza una riga del menu in base l'id
	@GetMapping("/menuLine/{id}")
	public String getMenuLine(@PathVariable("id") Long id, Model model) {
	    model.addAttribute("menuLine", this.menuLineService.findById(id));
	    return "menu/menuLine.html";
	}

}
