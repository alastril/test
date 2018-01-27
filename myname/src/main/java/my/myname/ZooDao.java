package my.myname;

import java.util.List;

public interface ZooDao {

	public Zoo save(Zoo zoo);
	public List<Zoo> getListZoo();
	public void deleteZoo(Zoo zoo);
	public Animals save(Animals animal) ;
	public List<Animals> saveJTA(Animals animal, Animals dublicate) ;
	public Zoo getZooById(Long id);
	public List<Animals> getListAnimals();
}
