package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Reservation;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReservationService;
import it.uniroma3.siw.service.ReservationService.Slot;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/reservations")	//indica che tutti i mapping descritti in questocontroller avranno come radice /reservations
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CredentialsService credentialsService;
    
    @GetMapping						//mapping base di /reservations
    public String showReservationHome(Model model) {
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();	//
        UserDetails ud = (UserDetails) auth.getPrincipal();								//
        Credentials creds = this.credentialsService.getCredentials(ud.getUsername());	//solito procedimento per richiamare le credenziali tramite il SecurityContextHolder
        User user = creds.getUser();

        List<Reservation> reservations = this.reservationService.getByUser(user);
        model.addAttribute("reservations", reservations);
        model.addAttribute("today", LocalDate.now());
        return "reservation/reservationHome";
    }

    @GetMapping("/add")
    public String showReservationForm(
        @RequestParam(name="date", required=false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        Model model) {
    	
        LocalDate chosen = (date != null) ? date : LocalDate.now();

        Reservation reservation = new Reservation();
        reservation.setDate(chosen);

        model.addAttribute("reservation", reservation);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("slots", reservationService.getSlotAvailability(chosen));
        return "reservation/reservationForm";
    }


    @PostMapping("/add")
    public String processAddReservation(
            @ModelAttribute("reservation") @Valid Reservation reservation,
            BindingResult br,
            Model model) {

        // 1) validazione form base
        if (br.hasErrors()) {
            model.addAttribute("slots", reservationService.getSlotAvailability(reservation.getDate()));
            return "reservation/reservationForm";
        }

        // 2) controllo capienza
        int remaining = reservationService.getSlotAvailability(reservation.getDate()).stream()
                             .filter(s -> s.time().equals(reservation.getHour()))
                             .findFirst()
                             .map(Slot::remaining)
                             .orElse(0);
        if (reservation.getNumberOfPeople() > remaining) {
            br.rejectValue("hour", "full", "Posti insufficienti per questo orario");
            model.addAttribute("slots", reservationService.getSlotAvailability(reservation.getDate()));
            return "reservation/reservationForm";
        }

     // 3) controllo se l'utente ha già prenotato in quella data
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext()
                                        .getAuthentication().getPrincipal();
        User user = credentialsService.getCredentials(ud.getUsername()).getUser();

        boolean alreadyBooked = reservationService.getByUser(user).stream()
            .anyMatch(r -> r.getDate().equals(reservation.getDate()));

        if (alreadyBooked) {
            br.rejectValue("date", "duplicate", "Hai già una prenotazione per questa data");
            model.addAttribute("slots", reservationService.getSlotAvailability(reservation.getDate()));
            return "reservation/reservationForm";
        }

        // 4) associa utente e salva
        reservation.setUser(user);
        reservationService.save(reservation);

        return "redirect:/reservations";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findById(id);
        
        if (reservation != null) {
            // Verifica che l'utente loggato sia il proprietario della prenotazione
            UserDetails ud = (UserDetails) SecurityContextHolder.getContext()
                                        .getAuthentication().getPrincipal();
            User user = credentialsService.getCredentials(ud.getUsername()).getUser();

            if (reservation.getUser().equals(user)) {
                reservationService.delete(reservation);
            }
        }

        return "redirect:/reservations";
    }

}
