package my.myname;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.convert.ConversionService;

import my.myname.aop.AspectTest;
import my.myname.aop.PointCut;
import my.myname.aop.SimpleBean;
import my.myname.converters.AnotherTypeForConvert;
import my.myname.crud_spr_data.AnimalsService;
import my.myname.crud_spr_data.FoodService;
import my.myname.crud_spr_data.ZooDao;
import my.myname.entity.Animals;
import my.myname.entity.Food;
import my.myname.entity.Zoo;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Throwable {
		ApplicationContext ap = new GenericXmlApplicationContext("classpath:app-config.xml");
		
		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		FoodService fs = (FoodService) ap.getBean("FoodService");

		// callZooSave(ap);
		FoodSaveCall(ap);
		converterCall(ap);
		AopCall(ap);
	}

	public static void AopCall(ApplicationContext ap) {
		SimpleBeanImpl sb = (SimpleBeanImpl)ap.getBean("simpleBeanImpl");
		sb.sayHello("Grisha");
		// ProxyFactory pf = new ProxyFactory();
		// pf.setTarget(sb);
		// pf.addAdvice(new PointCut());
		// SimpleBeanImpl aopBean = (SimpleBeanImpl)pf.getProxy();
		// System.out.println(aopBean.sayHello("Grisha!1!11"));
		// System.out.println(aopBean.sayFuck("Grisha!1!11"));
	}

	public static void ZooSaveCall(ApplicationContext ap) throws Throwable {
		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		Zoo zoo = new Zoo();
		zoo.setName("menskiy");
		zoo.setDateCreation(new Date());
		Animals animalSobaka = new Animals();
		animalSobaka.setName("sobaka");
		Food food = new Food();
		food.setName("wiskas");
		Food food2 = new Food();
		food2.setName("milk");

		Animals animalKot = new Animals();
		animalKot.setName("kot");
		animalKot.addFood(food);
		animalKot.addFood(food2);

		zoo.getAnimalsList().add(animalSobaka);
		zoo.getAnimalsList().add(animalKot);

		Zoo zooClone = (Zoo) zoo.clone();

		zd.save(zoo);
		zd.save(zooClone);

		List<Zoo> zl = zd.getListZoo();
		System.out.println(zl.size());
		for (Zoo z : zl) {
			System.out.println(z.toString());
			for (Animals animals : z.getAnimalsList()) {
				System.out.println(animals.toString());
			}
		}
		zd.deleteZoo(zoo);
	}

	/**
	 * not working EntityManager with many-to-one relation when we try save
	 * children with parent(error: first save parent than children) <br/>
	 * but with session is okay
	 * 
	 * @param ap
	 * @param fs
	 * @param zd
	 * @throws Throwable
	 */
	public static void FoodSaveCall(ApplicationContext ap) throws Throwable {

		// ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		FoodService fs = (FoodService) ap.getBean("FoodService");
		AnimalsService as = (AnimalsService) ap.getBean("AnimalsService");
		Food food = ap.getBean(Food.class);
		food.setName("wiskas");
		Food food2 = ap.getBean(Food.class);
		food2.setName("milk");

		Animals animalKot = ap.getBean(Animals.class);
		animalKot.setName("kot");
		Animals animalKot2 = ap.getBean(Animals.class);
		animalKot2.setName("kot2");
		animalKot.addFood(food2);
		animalKot2.addFood(food);
		// food2.setAnimals(animals);
		// zd.save(animalKot);
		// zd.save(animalKot2);
		Animals test = food.getAnimals();
		
		if (test != null) {
			as.save(food.getAnimals());
		} else {
			fs.save(food);
		}
		
		test = food2.getAnimals();
		if (test != null) {
			as.save(food2.getAnimals());
		} else {
			fs.save(food2);
		}

		List<Food> foodList = fs.findAll();
		for (Food fd : foodList) {
			System.out.println(fd.toString());
		}

	}

	public static void JTACall(ApplicationContext ap) throws Throwable {

		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		Animals an = new Animals();
		an.setName("sobaken");
		Animals an2 = new Animals();
		an2.setName("kotyara");
		zd.saveJTA(an, an2);
		zd.saveJTA((Animals) an.clone(), (Animals) an2.clone());
		List<Animals> al = zd.getListAnimals();
		for (Animals a : al) {
			System.out.println(a.toString());
			for (Food fod : a.getFoodList()) {
				System.out.println(fod.toString());
			}
		}
	}

	
	public static void converterCall(ApplicationContext ap) {
		SimpleBeanImpl smpl = (SimpleBeanImpl)ap.getBean("dateConvert");
		System.out.println(smpl);
		ConversionService conversionService = ap.getBean(ConversionService.class);
		AnotherTypeForConvert anotherContact = ap.getBean(AnotherTypeForConvert.class);
		anotherContact.setId(25);
		anotherContact.setName("Vasya Pypkin");
		anotherContact.setDateTime(new DateTime());
		smpl= conversionService.convert(anotherContact, SimpleBeanImpl.class); 
		System.out.println(smpl);
	}
}
