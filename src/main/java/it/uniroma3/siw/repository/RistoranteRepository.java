package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Ristorante;

public interface RistoranteRepository extends CrudRepository<Ristorante, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
}
