package my.myname.mvc.security.daoImpl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.mvc.security.dao.UserRoleDao;
import my.myname.mvc.security.dao.repository.UserRoleRepository;
import my.myname.mvc.security.entity.UserRole;

@Repository("UserRoleRepository")
@Transactional("transactionManagerJPA")
public class UserRoleDaoImpl implements UserRoleDao{

	@Autowired
	private UserRoleRepository userRoleRep;
	
	@Override
	public List<UserRole> findAll() {
		List<UserRole> listRes= new LinkedList<>();
		userRoleRep.findAll().iterator().forEachRemaining(listRes::add);
		return listRes;
	}

	@Override
	public UserRole save(UserRole userRole) {
		return userRoleRep.save(userRole);
	}

	@Override
	public void delete(UserRole userRole) {
		userRoleRep.delete(userRole);
	}

}
