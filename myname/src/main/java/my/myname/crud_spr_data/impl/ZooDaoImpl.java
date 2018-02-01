package my.myname.crud_spr_data.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.entity.Animals;
import my.myname.entity.Food;
import my.myname.entity.Zoo;

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
	@PersistenceContext(unitName="emfB")
	EntityManager emB;
	
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
		return animals;
	}

	@Override
	public Food save(Food food) {
			getSessionFactory().getCurrentSession().saveOrUpdate(food);
		return food;
	}
	
	@Transactional(transactionManager = "transactionМanagerAtomicos")
	public List<Animals> saveJTA(Animals animals, Animals duplicate) {
		List<Animals> anls = new ArrayList<>();
		if (!emA.isJoinedToTransaction() && !emB.isJoinedToTransaction()) {
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
