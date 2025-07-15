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

        LocalTime current = start;
        while (!current.isAfter(end)) {
            // 1) recupero tutte le prenotazioni per questa data e quest’ora
            List<Reservation> existingReservations =
                reservationRepository.findByDateAndHour(date, current);

            // 2) sommo manualmente quante persone ci sono già prenotate
            int used = 0;
            for (Reservation r : existingReservations) {
                used += r.getNumberOfPeople();
            }

            // 3) calcolo i posti rimanenti
            int remaining = MAX_PER_SLOT - used;

            // 4) aggiungo lo slot alla lista
            slots.add(new Slot(current, remaining));

            // 5) passo al prossimo orario (+30 minuti)
            current = current.plusMinutes(30);
        }

        return slots;
    }


    /*questo quì è la definizione di una classe chiamata Slot che ci serve per
     * definire degli intervalli di tempo in cui prenotare che abbiano un int 
     * di posti rimanenti prenotabili*/
    public static record Slot(LocalTime time, int remaining) {}


    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public List<Reservation> getByDate(LocalDate date) {
        return reservationRepository.findByDate(date);
    }


}
