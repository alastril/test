package my.myname.crud_spr_data;


import org.springframework.data.repository.CrudRepository;

import my.myname.entity.Food;


public interface FoodRepository extends CrudRepository<Food, Long> {

}
