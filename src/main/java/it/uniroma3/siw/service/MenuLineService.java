package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.MenuLine;
import it.uniroma3.siw.repository.MenuLineRepository;

@Service
public class MenuLineService {

    @Autowired
    private MenuLineRepository menuLineRepository;

	public MenuLine findById(Long id) {
		return menuLineRepository.findById(id).get();
	}
    
}
