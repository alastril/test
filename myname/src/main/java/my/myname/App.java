package my.myname;

import java.util.Date;
import java.util.List;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

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
//       animalKot.addFood(food);
//       animalKot.addFood(food2);
       
       zoo.getAnimalsList().add(animalSobaka);
       zoo.getAnimalsList().add(animalKot);
       //zd.save(zoo);
       zd.save(animalKot);
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
       List<Animals> al = zd.getListAnimals();
       
       for(Animals a: al){
    	   System.out.println(a.toString());
    	   for(Food fod: a.getFoodList()){
    	    System.out.println(fod.toString());
    	   }
       }
       
    }
}
