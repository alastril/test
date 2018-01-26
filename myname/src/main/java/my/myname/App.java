package my.myname;

import java.util.Date;
import java.util.List;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import my.myname.aop.AspectTest;
import my.myname.aop.PointCut;
import my.myname.aop.SimpleBean;
import my.myname.aop.SimpleBeanImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws CloneNotSupportedException
    {
       ApplicationContext ap = new GenericXmlApplicationContext("classpath:app-config.xml");
       ZooDao zd = (ZooDao) ap.getBean("ZooDao");
       FoodService fs = (FoodService) ap.getBean("FoodService");

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
       animalKot.getFoodList().add(food);
       animalKot.getFoodList().add(food2);
       
       zoo.getAnimalsList().add(animalSobaka);
       zoo.getAnimalsList().add(animalKot);
       Animals an = new Animals();
       an.setName("kot");
       zd.save(zoo);
       
       //zd.save(animalKot);
//       zoo.setId(null);

//	   zoo = (Zoo)zoo.clone();
//       zoo.getAnimalsList().clear();
//       zoo.getAnimalsList().add((Animals) animalSobaka.clone());
//       zoo.getAnimalsList().add((Animals) animalKot.clone());
//       zd.save(zoo);
      
//       List<Zoo> zl = zd.getListZoo();
//       System.out.println(zl.size());
//       for(Zoo z: zl){
//    	   System.out.println(z.toString());
//    	   for(Animals animals: z.getAnimalsList()){
//    	    System.out.println(animals.toString());
//    	   }
//       }
       fs.save((Food)food.clone());
       fs.save((Food)food2.clone());
       //List<Animals> al = zd.getListAnimals();
       zd.save(an);
       List<Food> foodList = fs.findAll();
       for(Food fd: foodList){
    	   System.out.println(fd.toString());
    	   
       }
//       for(Animals a: al){
//    	   System.out.println(a.toString());
//    	   for(Food fod: a.getFoodList()){
//    	    System.out.println(fod.toString());
//    	   }
//       }
       callAop( ap);
    }
    
   public static void callAop(ApplicationContext ap){
	    SimpleBeanImpl sb = ap.getBean(SimpleBeanImpl.class);
    	sb.sayHello("Grisha");
//    	ProxyFactory pf = new ProxyFactory();
//    	pf.setTarget(sb);
//    	pf.addAdvice(new PointCut());
//    	SimpleBeanImpl aopBean = (SimpleBeanImpl)pf.getProxy();
//    	System.out.println(aopBean.sayHello("Grisha!1!11"));
//    	System.out.println(aopBean.sayFuck("Grisha!1!11"));
    }
}
