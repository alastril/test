package my.myname.crud_spr_data;


import org.springframework.data.repository.CrudRepository;

import my.myname.crud_spr_data.entity.Animals;


public interface AnimalsRepository extends CrudRepository<Animals, Long> {

}
