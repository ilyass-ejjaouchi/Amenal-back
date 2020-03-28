package org.amenal.rest.representation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategorieArticlePresentation {
	private Integer id;
	private String categorie;
	private Boolean isAssoWithProjet;
	private List<ArticlePresentation> articles = new ArrayList<ArticlePresentation>();
}
