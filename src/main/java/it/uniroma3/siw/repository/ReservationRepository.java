package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Reservation;
import it.uniroma3.siw.model.User;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByDataAndOra(LocalDate data, LocalTime ora);
}
