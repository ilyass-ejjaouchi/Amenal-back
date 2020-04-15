package org.amenal.metier;

import java.util.List;
import java.util.stream.Collectors;

import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
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

		if (name.trim() == "")
			throw new BadRequestException("l'unite ne doit pas etre vide! ");

		Unite u = uniteRepository.findByUnite(name.trim().toUpperCase());

		if (u != null)
			if (u.getShowUnite())
				throw new BadRequestException("cette Unite est deja ajouté!");
			else
				u.setShowUnite(true);
		else {
			u = new Unite();
			u.setShowUnite(true);
			u.setUnite(name.trim().toUpperCase());
			uniteRepository.save(u);
		}

	}

	public List<String> getUnite() {

		return uniteRepository.findByShowUnite(true).stream().map(u -> u.getUnite()).collect(Collectors.toList());
	}

	public void deleteUnite(String unite) {

		Unite u = uniteRepository.findByUnite(unite.trim().toUpperCase());

		if (u == null)
			throw new NotFoundException("l'unite est introuvable");
		if (!u.getArticles().isEmpty()) {
			Boolean dlt = false;
			for (Article a : u.getArticles()) {
				if (a.getShowArt()) {
					dlt = true;
					break;
				}
			}
			if (dlt)
				throw new BadRequestException("l'unite [ " + unite + " ] Est deja associer a des articles!");
			else
				u.setShowUnite(false);
		} else

			u.setShowUnite(false);
	}

}
