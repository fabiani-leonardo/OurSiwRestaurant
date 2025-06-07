
package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Review;

public interface RecensioneRepository extends CrudRepository<Review, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
}
