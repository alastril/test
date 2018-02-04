package my.myname.crud_spr_data.interfaces;


import java.util.List;

import my.myname.crud_spr_data.entity.Food;


public interface FoodService  {

	public List<Food> findAll();
	public Food save(Food food);
}
