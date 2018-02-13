package my.myname.crud_spr_data.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.crud_spr_data.FoodRepository;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.interfaces.FoodService;


@Repository(value="FoodService")
@Transactional
public class FoodServiceImpl implements FoodService  {
	
	@Autowired
	private FoodRepository foodRepository;

	@Override
	public List<Food> findAll() {
		List<Food> listRes = new ArrayList<Food>();
		foodRepository.findAll().iterator().forEachRemaining(listRes::add);
		return listRes;
	}

	@Override
	public Food save(Food food) {
		return foodRepository.save(food);
	}
}
