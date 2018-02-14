package my.myname.mvc.security.daoImpl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.mvc.security.dao.UserDao;
import my.myname.mvc.security.dao.repository.UserRepository;
import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;

@Transactional("transactionManagerJPA")
@Repository("UserRepository")
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserRepository userRep;
	@Autowired
	private SessionFactory sessFac;


	@Override
	public List<User> findAll() {
		List<User> listRes = new LinkedList<>();
		userRep.findAll().iterator().forEachRemaining(listRes::add);
		return listRes;
	}

	@Override
	public User save(User user) {
		return userRep.save(user);
	}

	@Override
	public void delete(User user) {
		userRep.delete(user);
	}

	@Override
	@Transactional("transactionManagerSession")
	public User getUserByName(String userName) {
		return (User) sessFac.getCurrentSession().getNamedQuery("GetUserByName").setParameter("userName", userName)
				.getSingleResult();
	}


}
