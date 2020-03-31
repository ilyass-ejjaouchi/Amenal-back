package org.amenal.metier;

import java.util.List;
import java.util.stream.Collectors;

import org.amenal.dao.UniteRepository;
import org.amenal.entities.Unite;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UniteMetier {

	@Autowired
	UniteRepository uniteRepository;

	public void AddUnite(String name) {

		Unite u = new Unite();
		if (name.trim() == "")
			throw new BadRequestException("l'unite ne doit pas etre vide! ");
		u.setUnite(name);
		uniteRepository.save(u);
	}

	public List<String> getUnite() {

		return uniteRepository.findAll().stream().map(u -> u.getUnite()).collect(Collectors.toList());
	}

	public void deleteUnite(String unite) {

		Unite u = uniteRepository.findByUnite(unite.trim().toUpperCase());

		if (u == null)
			throw new NotFoundException("l'unite est introuvable");
		if (!u.getArticles().isEmpty()) {
			throw new BadRequestException("l'unite [ " + unite + " ] Est deja associer a des articles!");
		}

		uniteRepository.delete(u);

	}

}
