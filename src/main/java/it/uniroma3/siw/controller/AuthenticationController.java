package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credentials.ADMIN_ROLE)) {
			if (credentials.getMustChange()) {
			    return "admin/changeCredentials";	//pagina e metodo da implementare
			}
			return "admin/adminHome.html";
		}
		
		model.addAttribute("userDetails", userDetails);	//aggiungo ad home.html la possibilità di utilizzare i dati dell'utente autenticato presenti in userDetails
		return "home.html";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
							   BindingResult userBindingResult,
							   @Valid @ModelAttribute("credenziali") Credentials credentials,
							   BindingResult credenzialiBindingResult,
							   Model model) {

		if (!userBindingResult.hasErrors() && !credenzialiBindingResult.hasErrors()) {
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful";
		}
		return "formRegisterUser";
	}
	
	/*
	@GetMapping("/changeCredentials")
	public String showModificaCredenziali(Model model, Principal principal) {
	    Credentials credentials = credentialsService.getCredentials(principal.getName());
	    model.addAttribute("credentials", credentials);
	    return "formModificaCredenziali";
	}

	@PostMapping("/modificaCredenziali")
	public String modificaCredenziali(@ModelAttribute("credentials") Credentials newCreds,
	                                  @RequestParam("confermaPassword") String confermaPassword,
	                                  Model model, Principal principal) {
	    Credentials oldCreds = credentialsService.getCredentials(principal.getName());

	    // Validazione password
	    if (!newCreds.getPassword().equals(confermaPassword)) {
	        model.addAttribute("error", "Le password non coincidono.");
	        return "formModificaCredenziali";
	    }

	    // Validazione username già usato (opzionale)
	    if (!oldCreds.getUsername().equals(newCreds.getUsername()) &&
	        credentialsService.getCredentials(newCreds.getUsername()) != null) {
	        model.addAttribute("error", "Username già in uso.");
	        return "formModificaCredenziali";
	    }

	    // Aggiornamento
	    oldCreds.setUsername(newCreds.getUsername());
	    oldCreds.setPassword(newCreds.getPassword());
	    oldCreds.setMustChange(false);
	    credentialsService.saveCredentials(oldCreds);

	    return "redirect:/default"; // O pagina home
	
	
	
	
	//admin
	
	
	
	/*@GetMapping(value = "/admin/login")
	public String showLoginAdminForm(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getMustChange()) {
		    return "redirect:/admin/modificaCredenziali";
		}
		else {
			return "formLogin";
		}
		
	}*/
	
}
