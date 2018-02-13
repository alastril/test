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

public class ControllerZooTest {
	private static final Logger LOG = Logger.getLogger(ControllerZooTest.class);
	
	final List<Food> foodList = new ArrayList<>();
	final List<Zoo> zooList = new ArrayList<>();
	final List<Animals> animalsList = new ArrayList<>();
	
	@Before
	public void initArrays() {
		
		Zoo zoo = new Zoo();
		zoo.setId(345l);
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
			Zoo clone = (Zoo)zoo.clone();
			clone.setId(346l);
			zooList.add(clone);
		} catch (CloneNotSupportedException e) {
			LOG.error("ERROR!",e);
		}
	}
	
	@Test
	public void testGetXmlZoo() {
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
			assertEquals(2, Unmarsh.getZooList().size());
		} catch (Throwable e) {
			LOG.error("ERROR!",e);
		}
		
	}

	@Test
	public void testCreateJsonZoo() {

		ZooDao zooDao = Mockito.mock(ZooDao.class);
		final Zoo zoo = new Zoo();
		zoo.setId(777l);
		zoo.setName("Hello");
		zoo.setDateCreation(new DateTime());
		zooList.add(zoo);
		ControllerZoo controller = new ControllerZoo();
		MarshUnmarsh insertMarsh = Mockito.mock(MarshUnmarsh.class);
		ReflectionTestUtils.setField(controller, "zooDao", zooDao);
		Mockito.when(insertMarsh.getZooList()).thenAnswer(new Answer<List<Zoo>>() {

			@Override
			public List<Zoo> answer(InvocationOnMock invocation) throws Throwable {
				return zooList;
			}
		});
		
//		marshUnmarsh
		MarshUnmarsh resmarsh = new MarshUnmarsh();
		try {
			resmarsh = controller.setJsonZoo(insertMarsh);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		assertEquals(Long.valueOf(777l), resmarsh.getZooList().get(2).getId());
		assertEquals("Hello", resmarsh.getZooList().get(2).getName());
		assertEquals(3, zooList.size());
	}
}
