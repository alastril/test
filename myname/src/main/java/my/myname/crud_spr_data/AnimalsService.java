package my.myname.crud_spr_data;


import java.util.List;

import my.myname.entity.Animals;
import my.myname.entity.Food;


public interface AnimalsService  {

	public List<Animals> findAll();
	public Animals save(Animals animals);
}
