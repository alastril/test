package my.myname.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;

@Transactional("transactionManagerSession")
public class WriterUserItemImpl implements ItemWriter<User> {
	private static final Logger LOG = Logger.getLogger(WriterUserItemImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void write(List<? extends User> items) {
		Session sess = sessionFactory.getCurrentSession();
		for (User user : items) {
			user.setUserRoles(user.getUserRoles().stream().map(action -> {
				try {
					action = (UserRole) action.clone();
				} catch (CloneNotSupportedException e) {
					LOG.error("Error", e);
				}
				return action;
			}).collect(Collectors.toList()));
			sess.save(user);
		}
	}
}
