
package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	//aggiungeremo i metodi quando sapremo cosa ci serve
	public Optional<Credentials> findByUsername(String username);	//(autoimplementato grazie a Spring Data JPA), trova un oggetto credenziali del database dato uno username
	
}
