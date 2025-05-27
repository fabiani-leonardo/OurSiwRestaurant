package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
}
