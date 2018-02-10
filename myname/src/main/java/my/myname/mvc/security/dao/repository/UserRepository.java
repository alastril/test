package my.myname.mvc.security.dao.repository;


import org.springframework.data.repository.CrudRepository;

import my.myname.mvc.security.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
	

}
