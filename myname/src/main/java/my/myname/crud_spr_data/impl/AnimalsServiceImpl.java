package my.myname.crud_spr_data.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.crud_spr_data.AnimalsRepository;
import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.interfaces.AnimalsService;


@Repository(value="AnimalsService")
@Transactional("transactionManagerJPA")
public class AnimalsServiceImpl implements AnimalsService  {
	
	@Autowired
	private AnimalsRepository animalsRepository;

	@Override
	public List<Animals> findAll() {
		List<Animals> listRes = new ArrayList<Animals>();
		animalsRepository.findAll().iterator().forEachRemaining(listRes::add);
		return listRes;
	}

	@Override
	public Animals save(Animals animals) {
		Optional<Animals> anim = Optional.ofNullable(animals);
		anim.ifPresent(consumer -> {animalsRepository.save(consumer);});
		return animals;
	}
}
