

package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Reservation;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
}
