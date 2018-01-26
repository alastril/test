package my.myname;


import java.util.List;


public interface FoodService  {

	public List<Food> findAll();
	public Food save(Food food);
}
