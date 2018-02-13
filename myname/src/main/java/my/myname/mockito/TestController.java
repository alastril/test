package my.myname.mockito;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;

import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;
import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.mvc.ControllerZoo;
import my.myname.mvc.MarshUnmarsh;

public class TestController {
	private static final Logger LOG = Logger.getLogger(TestController.class);
	
	final List<Food> foodList = new ArrayList<>();
	final List<Zoo> zooList = new ArrayList<>();
	final List<Animals> animalsList = new ArrayList<>();
	
	@Before
	public void initArrays() {
		
		Zoo zoo = new Zoo();
		zoo.setName("menskiy");
		zoo.setDateCreation(new DateTime());
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
		
		zooList.add(zoo);
		foodList.add(food);
		foodList.add(food2);
		animalsList.add(animalKot);
		animalsList.add(animalSobaka);
		try {
			zooList.add((Zoo)zoo.clone());
		} catch (CloneNotSupportedException e) {
			LOG.error("ERROR!",e);
		}
	}
	
	@Test
	public void testServices() {
		ZooDao zooDao = Mockito.mock(ZooDao.class);
		MarshUnmarsh marshUnmarsh= Mockito.mock(MarshUnmarsh.class);
		Mockito.when(marshUnmarsh.getZooList()).thenReturn(zooList);
		Mockito.when(zooDao.getListZoo()).thenReturn(zooList);
		ControllerZoo contZoo = new ControllerZoo();
		ReflectionTestUtils.setField(contZoo, "zooDao", zooDao);
		ReflectionTestUtils.setField(contZoo, "rootDocument", marshUnmarsh);
		
		ExtendedModelMap uiModelMap = new ExtendedModelMap();
		try {
			uiModelMap.addAttribute("xmlZoo", contZoo.getXmlZoo());
			MarshUnmarsh Unmarsh = (MarshUnmarsh)uiModelMap.get("xmlZoo");
			LOG.info(Unmarsh.getZooList());
			assertEquals(2, Unmarsh.getZooList().size());
		} catch (Throwable e) {
			LOG.error("ERROR!",e);
		}
		
	}

	@Test
	public void testCreate() {
		final Zoo zoo = new Zoo();
		zoo.setName("menskiy");
		zoo.setDateCreation(new DateTime());
		ZooDao zooDao = Mockito.mock(ZooDao.class);
		Mockito.when(zooDao.save(zoo)).thenAnswer(new Answer<Zoo>() {
			public Zoo answer(InvocationOnMock invocation) throws Throwable {
				zooList.add(zoo);
				return zoo;
			}
		});
			
		ControllerZoo controller = new ControllerZoo();
		ReflectionTestUtils.setField(controller, "contactService", zooDao);
//		marshUnmarsh
//		Zoo zooNew = controller.setJsonZoo(marshUnmarsh);
//		assertEquals(Long.valueOf(999l), contact.getid());
//		assertEquals("Rod", contact.getFirstName());
//		assertEquals("Johnson", contact.getLastName());
//		assertEquals(2, contacts.size());
	}
}
