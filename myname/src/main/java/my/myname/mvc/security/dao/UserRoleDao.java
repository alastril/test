package my.myname.mvc.security.dao;

import java.util.List;

import my.myname.mvc.security.entity.UserRole;

public interface UserRoleDao {

	public List<UserRole> findAll();
	public UserRole save(UserRole user);
	public void delete(UserRole user);
}
