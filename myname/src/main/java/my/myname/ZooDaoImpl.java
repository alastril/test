package my.myname;

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
	@Autowired
	@Qualifier("emfA")
	SessionFactory sessionFactoryTest;
	@Autowired
	@Qualifier("emfB")
	SessionFactory sessionFactoryTestB;
//	@Autowired
//	@Qualifier("emfA")
//	EntityManager emA;
	
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManager em;
	
	
	@Transactional(transactionManager="transactionManagerJPA", propagation=  Propagation.REQUIRED)
	public Zoo save(Zoo zoo) {

		if(zoo.getId()!=null && em.getReference(Zoo.class, zoo.getId())!=null){
		 em.merge(zoo);
		} else{
			em.persist(zoo);
		}
		//getSessionFactory().getCurrentSession().saveOrUpdate(zoo);
		return zoo;
	}

	@Transactional(transactionManager="transactionМanagerAtomicos", propagation=  Propagation.REQUIRES_NEW)
	public Animals save(Animals animals) {
//		System.out.println(emA.getReference(Food.class, 1l));
		
		try {
			sessionFactoryTest.getCurrentSession().saveOrUpdate(animals.clone());
			sessionFactoryTestB.getCurrentSession().saveOrUpdate((Animals)animals.clone());
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		emA.merge(animals);
		//getSessionFactory().getCurrentSession().saveOrUpdate(animals);
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


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
