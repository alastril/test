package my.myname.crud_spr_data.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;
import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.jms.JmsProduser;

@Repository("ZooDao")
@Transactional(transactionManager ="transactionManagerSession")
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
	@PersistenceContext(unitName="emfB")
	EntityManager emB;
	@Autowired
	@Qualifier("emfC")
	EntityManager emC;
	@Autowired
	JmsProduser call;
	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManager em;
	
	@Override
	public Zoo save(Zoo zoo) {
		getSessionFactory().getCurrentSession().saveOrUpdate(zoo);
		return zoo;
	}
	@Override
	public Animals save(Animals animals) {
		getSessionFactory().getCurrentSession().saveOrUpdate(animals);
//		if(animals!=null)
//			throw new RuntimeException("3424");
//		
		return animals;
	}

	@Override
	public Food save(Food food) {
			getSessionFactory().getCurrentSession().saveOrUpdate(food);
		return food;
	}
	
	@Transactional(transactionManager = "transactionМanagerAtomicos", rollbackFor = Exception.class)
	public List<Animals> saveJTA(Animals animals, Animals animalFor2ndDB, Animals postgres) {
		List<Animals> anls = new ArrayList<>();
		if (!emA.isJoinedToTransaction() && !emB.isJoinedToTransaction()) {
			emA.joinTransaction();
			emB.joinTransaction();
			emC.joinTransaction();
		}

		if(animals.getId()==null && animalFor2ndDB.getId()==null && postgres.getId()==null){
			System.out.println("persist");
			emA.persist(animals);
			emB.persist(animalFor2ndDB);
			emC.persist(postgres);
			call.sendToQueue("JMS: JTA persist!!!!");
			call.sendToTopic("JMS: JTA persist!!!!");
		} else {
			System.out.println("merge");
			emA.merge(animals);
			emB.merge(animalFor2ndDB);
			emC.merge(postgres);
			call.sendToQueue("JMS: JTA merge!!!!");
			call.sendToTopic("JMS: JTA merge!!!!");
		}

//		int i = 1/0; //check for rollback
		anls.add(animals);
		anls.add(animalFor2ndDB);
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
//		em.joinTransaction();
//		em.remove(em.contains(zoo) ? zoo : em.merge(zoo));
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
