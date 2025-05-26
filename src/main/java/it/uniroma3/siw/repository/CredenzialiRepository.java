
package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Credenziali;

public interface CredenzialiRepository extends CrudRepository<Credenziali, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
	public Optional<Credenziali> findByUsername(String username);	//(autoimplementato grazie a Spring Data JPA), trova un oggetto credenziali del database dato uno username
	
}
