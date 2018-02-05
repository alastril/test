package my.myname.crud_spr_data.interfaces;


import java.util.List;

import my.myname.crud_spr_data.entity.Animals;


public interface AnimalsService  {

	public List<Animals> findAll();
	public Animals save(Animals animals);
	public void deleteAnimals(Animals animals);
}
