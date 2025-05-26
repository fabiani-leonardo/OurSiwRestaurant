package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;
import jakarta.transaction.Transactional;

@Service
public class CredenzialiService {

    @Autowired
    private CredenzialiRepository credenzialiRepository;
    
    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Aggiungeremo i metodi quando sapremo cosa ci serve
    
    
    /*chiede a credenzialiRepository di trovare un oggetto credenziali dato un id e lo restituisce*/
    @Transactional
    public Credenziali getCredenziali(Long id) {
        Optional<Credenziali> result = this.credenzialiRepository.findById(id);
        return result.orElse(null);
    }
    
    /*chiede a credenzialiRepository di trovare un oggetto credenziali dato uno username e lo restituisce*/
    @Transactional
    public Credenziali getCredenziali(String username) {
        Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
        return result.orElse(null);
    }

    /*chiee a credenzialiRepository di salvare l'oggetto Credenziali fornito ma solo dopo aver cryptato la password*/
    @Transactional
    public Credenziali saveCredenziali(Credenziali credentials) {
        credentials.setRuolo(Credenziali.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credenzialiRepository.save(credentials);
    }
}
