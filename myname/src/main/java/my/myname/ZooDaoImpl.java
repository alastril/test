package my.myname;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository("ZooDao")
@Transactional
public class ZooDaoImpl implements ZooDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Zoo save(Zoo zoo) {
		getSessionFactory().getCurrentSession().saveOrUpdate(zoo);
		return zoo;
	}
	@Transactional(value=TxType.REQUIRES_NEW)
	public Animals save(Animals animals) {
		getSessionFactory().getCurrentSession().saveOrUpdate(animals);
		return animals;
	}

	@SuppressWarnings("unchecked")
	public List<Zoo> getListZoo() {
		return (List<Zoo>)getSessionFactory().getCurrentSession().getNamedQuery("selectAll").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animals> getListAnimals() {
		return (List<Animals>)getSessionFactory().getCurrentSession().createQuery("select distinct a from Animals a left join fetch a.foodList f").list();
	}

	public void deleteZoo(Zoo zoo) {
		getSessionFactory().getCurrentSession().delete(zoo);
	}

	public Zoo getZooById(Long id) {
		return (Zoo) getSessionFactory().getCurrentSession().getNamedQuery("selectById").setParameter("id", id).uniqueResult();
	}

	@Bean("sessionFactory")
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
