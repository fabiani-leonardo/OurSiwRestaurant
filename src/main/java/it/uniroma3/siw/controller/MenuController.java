package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.MenuLine;
import it.uniroma3.siw.service.MenuLineService;
import it.uniroma3.siw.service.MenuService;
import jakarta.validation.Valid;

@Controller
public class MenuController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuLineService menuLineService;

	// riporta alla home del menu per l'admin
	@GetMapping(value = "/admin/menu/adminMenuHome")
	public String getAdminMenuHome() {
		return "admin/menu/adminMenuHome.html";
	}

	// l'admin visualizza una riga del menu in base l'id
	@GetMapping("admin/menu/menuLine/{id}")
	public String getMenuLine(@PathVariable("id") Long id, Model model) {
		model.addAttribute("menuLine", this.menuLineService.findById(id));
		return "admin/menu/menuLine.html";
	}

	// l'admin aggiunge una nuova menuLine
	@GetMapping("/admin/menu/formNewMenuLine")
	public String addNewMenuLine(Model model) {
		model.addAttribute("menuLine", new MenuLine());
		return "admin/menu/formNewMenuLine.html";
	}

	@PostMapping("/menuLine")
	public String newMenuLine(@Valid @ModelAttribute("menuLine") MenuLine menuLine, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) { // se ci sono errori
			return "admin/menu/formNewMenuLine.html";
		} else {
			// Con redirect: il controller invia un messaggio HTTP 302 al client
			// imponendogli di fare
			// una richiesta HTTP GET ad una risosta specificata
			this.menuLineService.save(menuLine);
			model.addAttribute("menuLine", menuLine);
			return "redirect:/admin/menu/menuLine/" + menuLine.getId();
		}
	}

	// Risponde con con una pagina che mostra la lista di tutti i film

	@GetMapping("/menuLine")
	public String showMenuLine(Model model) {

		// recupera tutte le menuLine dal database
		List<MenuLine> allMenuLines = (List<MenuLine>) this.menuLineService.getAllMenuLine();

		// Filtra le menuLine per categoria
		List<MenuLine> primi = allMenuLines.stream().filter(m -> m.getCategory().equalsIgnoreCase("primo")).toList();

		List<MenuLine> secondi = allMenuLines.stream().filter(m -> m.getCategory().equalsIgnoreCase("secondo"))
				.toList();

		List<MenuLine> dolci = allMenuLines.stream().filter(m -> m.getCategory().equalsIgnoreCase("dolce")).toList();

		List<MenuLine> bevande = allMenuLines.stream().filter(m -> m.getCategory().equalsIgnoreCase("bevanda"))
				.toList();

		// aggiungi le liste al model per Thymeleaf
		model.addAttribute("primi", primi);
		model.addAttribute("secondi", secondi);
		model.addAttribute("dolci", dolci);
		model.addAttribute("bevande", bevande);

		return "menu/menuHome.html";
	}

}
