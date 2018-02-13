package my.myname.mvc.security.dao.repository;


import org.springframework.data.repository.CrudRepository;

import my.myname.mvc.security.entity.UserRole;

public interface UserRoleRepository  extends CrudRepository<UserRole, Long>{
	
}
