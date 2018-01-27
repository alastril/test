package my.myname.crud_spr_data;


import java.util.List;

import my.myname.entity.Food;


public interface FoodService  {

	public List<Food> findAll();
	public Food save(Food food);
}
