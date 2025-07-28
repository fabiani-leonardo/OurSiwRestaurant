package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class ReviewController {
	  @Autowired
	    private ReviewService reviewService;

	    @Autowired
	    private CredentialsService credentialsService;

	    /**
	     * Metodo di supporto per recuperare l'utente attualmente loggato.
	     */
	    private User getCurrentUser() {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        UserDetails userDetails = (UserDetails) auth.getPrincipal();
	        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	        return credentials.getUser();
	    }

	    /**
	     * Mostra il form per aggiungere una nuova recensione.
	     * Se l'utente ha già scritto una recensione, mostra un messaggio di errore
	     * e la lista delle recensioni.
	     */
	    @GetMapping("/addReview")
	    public String addNewReview(Model model) {
	        User user = getCurrentUser();
	        Review myReview = reviewService.findByUser(user);

	        if (myReview != null) {
	            model.addAttribute("errorMessage", "Hai già scritto una recensione. Cancellala prima di poterne aggiungere un'altra.");
	            List<Review> allReviews = (List<Review>) reviewService.getAllReviews();
	            model.addAttribute("allReviews", allReviews);
	            model.addAttribute("myReview", myReview);
	            return "review/reviews.html";
	        }

	        model.addAttribute("review", new Review());
	        return "review/formNewReview.html";
	    }

	    /**
	     * Salva la recensione scritta dall'utente loggato.
	     */
	    @PostMapping("/addReview")
	    public String addReview(@ModelAttribute("review") Review review) {
	        User user = getCurrentUser();
	        review.setUser(user);
	        review.setDate(LocalDate.now()); // imposta la data corrente
	        reviewService.save(review);
	        return "redirect:/reviews";
	    }

	    /**
	     * Mostra la lista di tutte le recensioni.
	     * Se l'utente è autenticato, mostra anche la sua recensione (myReview).
	     * Se non è autenticato, mostra solo tutte le recensioni.
	     */
	    @GetMapping("/reviews")
	    public String showReviews(Model model) {
	        User user = null;
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
	            UserDetails userDetails = (UserDetails) auth.getPrincipal();
	            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	            user = credentials.getUser();
	        }

	        List<Review> allReviews = (List<Review>) reviewService.getAllReviews();
	        model.addAttribute("allReviews", allReviews);

	        if (user != null) {
	            Review myReview = reviewService.findByUser(user);
	            model.addAttribute("myReview", myReview);
	        }

	        return "review/reviews.html";
	    }

	    /**
	     * Elimina una recensione dato il suo id, solo se appartiene all'utente loggato.
	     */
	    @GetMapping("/deleteReview/{id}")
	    public String deleteReview(@PathVariable("id") Long id) {
	        User user = getCurrentUser();
	        Review review = reviewService.findById(id);

	        // Controllo di sicurezza: l'utente può cancellare solo la propria recensione
	        if (review != null && review.getUser().getId().equals(user.getId())) {
	        	   user.setReview(null); // stacco la review
	               credentialsService.saveUser(user); // salva l'utente aggiornato
	               // Grazie a orphanRemoval=true in User, Hibernate eliminerà la review orfana dal DB
	        }

	        return "redirect:/reviews";
	    }
	    
	    /*********************************ADMIN*****************************************/
	    
	    //l'admin vede le recensioni
	    @GetMapping("/admin/adminReview")
	    public String showAllReviewsForAdmin(Model model) {
	        List<Review> allReviews = (List<Review>) reviewService.getAllReviews();
	        model.addAttribute("allReviews", allReviews);
	        return "admin/review/adminReview.html";  // path del template senza estensione
	    }

}
