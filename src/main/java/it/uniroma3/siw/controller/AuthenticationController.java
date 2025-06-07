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
import org.springframework.web.bind.annotation.RequestParam;

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
		
		model.addAttribute("userDetails", userDetails);
		
		/*verifichiamo ora se l'utente che ha acceduto è un admin, se lo è, verifichiamo anche se
		 nelle sue credenziali la variabile mustChange (che indica se le sue credenziali vanno cambiate)
		 è impostata su true, in tal caso reindirizziamo l'utente a /changeCredentials per cambiare 
		 username e password*/
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			System.out.println("sono un amministratoreeee");
			if (credentials.getMustChange()) {
			    return "redirect:/changeCredentials";
			}else {
				System.out.println("stiamo andando alla adminHome");
				return "admin/adminHome";
			}
		}
		System.out.println("stiamo andando alla Home");
		return "home";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user,
							   BindingResult userBindingResult,
							   @Valid @ModelAttribute("credentials") Credentials credentials,
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
	
	
	@GetMapping("/changeCredentials")
	public String showModificaCredenziali(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    model.addAttribute("credentials", credentials);
	    return "formChangeCredentials";
	}

	@PostMapping("/modificaCredenziali")
	public String modificaCredenziali(@ModelAttribute("credentials") Credentials newCreds,
	                                  @RequestParam("confermaPassword") String confermaPassword,
	                                  Model model) {

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Credentials oldCreds = credentialsService.getCredentials(auth.getName());

	    // Validazione password
	    if (!newCreds.getPassword().equals(confermaPassword)) {
	        model.addAttribute("error", "Le password non coincidono.");
	        return "formChangeCredentials";
	    }

	    // Controllo username già esistente
	    Credentials esistenti = credentialsService.getCredentials(newCreds.getUsername());
	    if (!oldCreds.getUsername().equals(newCreds.getUsername()) && esistenti != null) {
	        model.addAttribute("error", "Username già in uso.");
	        return "formChangeCredentials";
	    }

	    // Aggiornamento e salvataggio (codifica avviene nel service)
	    oldCreds.setUsername(newCreds.getUsername());
	    oldCreds.setPassword(newCreds.getPassword());
	    oldCreds.setMustChange(false);
	    credentialsService.saveCredentials(oldCreds);

	    // Logout forzato per richiedere nuovo login con credenziali aggiornate
	    SecurityContextHolder.clearContext();

	    return "redirect:/login";
	}


	
	
	
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
