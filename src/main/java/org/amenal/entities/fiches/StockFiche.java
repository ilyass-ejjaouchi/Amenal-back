package org.amenal.entities.fiches;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.Stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("stock")
@Setter@Getter
@NoArgsConstructor
public class StockFiche  extends Fiche  {
	private String type = FicheTypeEnum.STK.getCode();
	
	@OneToMany(mappedBy = "stockFiche"  )
	List<Stock> stocks;

}
