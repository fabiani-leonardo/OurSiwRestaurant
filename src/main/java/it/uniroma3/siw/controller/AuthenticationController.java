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
		model.addAttribute("credenziali", new Credentials());
		return "formRegisterUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredenziali(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credentials.ADMIN_ROLE)) {
			return "admin/adminHome.html";
		}
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
			credentialsService.saveCredenziali(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful";
		}
		return "formRegisterUser";
	}
}
