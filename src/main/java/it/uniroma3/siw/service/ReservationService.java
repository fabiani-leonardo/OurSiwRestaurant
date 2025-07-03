package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Reservation;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReservationRepository;

@Service
public class ReservationService {

    private static final int MAX_PER_SLOT = 20;
	@Autowired
    private ReservationRepository reservationRepository;

    /** Restituisce tutte le prenotazioni per un dato utente */
    public List<Reservation> getByUser(User user) {
        return this.reservationRepository.findByUser(user);
    }

    /** Salva o aggiorna una prenotazione */
    public Reservation save(Reservation reservation) {
        return this.reservationRepository.save(reservation);
    }
    
    /** restituisce per ogni slot (LocalTime) i posti **rimanenti** */
    public List<Slot> getSlotAvailability(LocalDate date) {
        List<Slot> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(19, 0);
        LocalTime end   = LocalTime.of(22, 30);
        for (LocalTime t = start; !t.isAfter(end); t = t.plusMinutes(30)) {
            int used = reservationRepository.findByDataAndOra(date, t)
                                 .stream()
                                 .mapToInt(Reservation::getNumberOfPeople)
                                 .sum();
            slots.add(new Slot(t, MAX_PER_SLOT - used));
        }
        return slots;
    }

    /** DTO interno per Thymeleaf */
    public static record Slot(LocalTime time, int remaining) {}
}
