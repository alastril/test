package my.myname.crud_spr_data;


import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.entity.Food;


@Repository(value="FoodService")
@Transactional("transactionManagerJPA")
public class FoodServiceImpl implements FoodService  {
	
	@Autowired
	private FoodRepository foodRepository;
	@Autowired
	private AnimalsRepository animalsRepository;

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