package my.myname.mvc.security.dao;

import java.util.List;

import my.myname.mvc.security.entity.User;

public interface UserDao {

	public List<User> findAll();
	public User save(User user);
	public void delete(User user);
	public User getUserByName(String userName);
}
