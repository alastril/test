package my.myname;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;

@Component
@Profile(value= {"work"})
public class FillDbData implements InitializingBean{

	@Autowired
	private SessionFactory sessFac;
	
	@Transactional("transactionManagerSession")
	@Override
	public void afterPropertiesSet() throws Exception {
		Session sess = sessFac.openSession();
		try {
			sess.beginTransaction();
			User user = new User();
			user.setUserName("pasha");
			user.setPassWord("123456");
			UserRole ur = new UserRole();
			ur.setRoleName("USER");
			user.getUserRoles().add(ur);
			ur = new UserRole();
			ur.setRoleName("ADMIN");
			user.getUserRoles().add(ur);
			sess.save(user);
			sess.getTransaction().commit();
			sess.close();
		} catch (Exception ex) {
			sess.getTransaction().rollback();
		}finally {
			sess.close();
		}
	}

}
