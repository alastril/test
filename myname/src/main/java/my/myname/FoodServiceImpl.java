package my.myname;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository(value="FoodService")
@Transactional("transactionManagerJPA")
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
