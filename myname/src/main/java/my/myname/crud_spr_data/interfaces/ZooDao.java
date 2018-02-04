package my.myname.crud_spr_data.interfaces;

import java.util.List;

import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;

public interface ZooDao {

	public Zoo save(Zoo zoo);
	public List<Zoo> getListZoo();
	public void deleteZoo(Zoo zoo);
	public Animals save(Animals animal) ;
	public Food save(Food food) ;
	public List<Animals> saveJTA(Animals animal, Animals dublicate) ;
	public Zoo getZooById(Long id);
	public List<Animals> getListAnimals();
}
