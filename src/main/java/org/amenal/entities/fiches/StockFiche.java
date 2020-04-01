package org.amenal.entities.fiches;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("stock")
@Setter@Getter
@NoArgsConstructor
public class StockFiche  extends Fiche  {
	private String type = FicheTypeEnum.STK.getCode();

}
