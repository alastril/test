package my.myname;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("ZooDao")
@Transactional("transactionManagerSession")
public class ZooDaoImpl implements ZooDao {

	@Autowired
	SessionFactory sessionFactory;
//	@Autowired
//	@Qualifier("emfA")
//	SessionFactory sessionFactoryTest;
//	@Autowired
//	@Qualifier("emfB")
//	SessionFactory sessionFactoryTestB;
	@PersistenceContext(unitName="emfA")
	EntityManager emA;
	@Autowired
	@PersistenceContext(unitName="emfB")
	EntityManager emB;
	
	
	public Zoo save(Zoo zoo) {

		getSessionFactory().getCurrentSession().saveOrUpdate(zoo);
		return zoo;
	}

	@Transactional(transactionManager="transactionМanagerAtomicos", propagation=  Propagation.REQUIRED)
	public Animals save(Animals animals) {
		
		emA.joinTransaction();
		emB.joinTransaction();
		try {
//			sessionFactoryTest.getCurrentSession().saveOrUpdate(animals.clone());
//			sessionFactoryTestB.getCurrentSession().saveOrUpdate((Animals)animals.clone());
			emA.persist(animals);
			emB.persist((Animals)animals.clone());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		emA.merge(animals);
		//getSessionFactory().getCurrentSession().saveOrUpdate(animals);
		return animals;
	}
	
	
	@Transactional(transactionManager = "transactionМanagerAtomicos", propagation = Propagation.REQUIRED)
	public List<Animals> saveJTA(Animals animals, Animals duplicate) {
		List<Animals> anls = new ArrayList<>();
		if (!emA.isJoinedToTransaction() || !emB.isJoinedToTransaction()) {
			emA.joinTransaction();
			emB.joinTransaction();
		}
		if(animals.getId()==null && animals.getId()==null){
			System.out.println("persist");
			emA.persist(animals);
			emB.persist(duplicate);
		} else {
			System.out.println("merge");
			emA.merge(animals);
			emB.merge(duplicate);
		}
		anls.add(animals);
		anls.add(duplicate);
		return anls;
	}

	@SuppressWarnings("unchecked")
	public List<Zoo> getListZoo() {
		return (List<Zoo>)getSessionFactory().getCurrentSession().getNamedQuery("selectAll").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Animals> getListAnimals() {
		return (List<Animals>)getSessionFactory().getCurrentSession().
				createQuery("select distinct a from Animals a left join fetch a.foodList f").list();
	}

	public void deleteZoo(Zoo zoo) {
		getSessionFactory().getCurrentSession().delete(zoo);
	}

	public Zoo getZooById(Long id) {
		return (Zoo) getSessionFactory().getCurrentSession().getNamedQuery("selectById").setParameter("id", id).uniqueResult();
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
//	public EntityManager getEm() {
//		return em;
//	}
//	public void setEm(EntityManager em) {
//		this.em = em;
//	}

}
