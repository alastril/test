package my.myname.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;

@Component
public class UserItemProcessor implements ItemProcessor<User, User>{
	private static final Logger LOG = Logger.getLogger(UserItemProcessor.class);
	@Override
	public User process(User arg0) throws Exception {
//		arg0.setId(null);
		arg0.setUserName(arg0.getUserName().toUpperCase());
		arg0.setPassWord(arg0.getPassWord().toUpperCase());
//		for(UserRole userRole: arg0.getUserRoles()) {
//			userRole.setId(null);
//		}
		LOG.info(arg0.toString());
		return arg0;
	}

}
