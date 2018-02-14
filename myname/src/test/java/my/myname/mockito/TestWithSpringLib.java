package my.myname.mockito;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import my.myname.mvc.security.dao.UserDao;
import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:app-config-test.xml")
@Transactional("transactionManagerJPA")
@TestExecutionListeners({ExecutionListener.class})
@ActiveProfiles("test") 
public class TestWithSpringLib extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	UserDao userDao;
	
	@Test
	@DataSet(fieldPath = "/users.xlsx")
	public void testGetUsers() {
		List<User> listUsers = userDao.findAll();
		assertEquals(3, listUsers.size());
	}
	
	@Test
	@DataSet(fieldPath = "/users.xlsx")
	public void testSaveUser() {
		User userToSave= new User();
		userToSave.setPassWord("67567");
		userToSave.setUserName("forthUser");
		UserRole userRole = new UserRole();
		userRole.setRoleName("TEST");
		userToSave.getUserRoles().add(userRole);
		userDao.save(userToSave);
		List<User> listUsers = userDao.findAll();
		assertEquals(4, listUsers.size());
		assertEquals("67567", listUsers.get(3).getPassWord());
		assertEquals("forthUser", listUsers.get(3).getUserName());
		assertEquals("TEST", listUsers.get(3).getUserRoles().get(0).getRoleName());
		
	}
}
