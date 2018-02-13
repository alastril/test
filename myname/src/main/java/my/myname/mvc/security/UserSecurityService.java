package my.myname.mvc.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import my.myname.mvc.security.dao.UserDao;
import my.myname.mvc.security.entity.User;


/**
 * The Class UserSecurityService.
 * This bean use for spring security authorization
 */
@Component
public class UserSecurityService implements UserDetailsService{

	/** The user dao. */
	@Autowired
	private UserDao userDao;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getUserByName(username);
				UserDetails  userSecurity = 
				org.springframework.security.core.userdetails.User.
				builder().
				username(username).
				password(user.getPassWord()).
				disabled(!user.getEnable()).
				accountExpired(false).
				accountLocked(false).
				credentialsExpired(false).
				roles(user.getRolesNames()).
				build();
		return userSecurity;
	}

}
