package myname;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import my.myname.junit.ForJunit;

public class ForJunitTest {

	
	@Test
	public void testRandomCount() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(10,200).size()>0);      
		
		///terter
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRandomRangeIllegalArg() {
		ForJunit ju = new ForJunit();
		System.out.println(ju.getRandomByCount(100,10)); 
	}
	
	@Test
	public void testRandomCountNotNull() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(0,0).isEmpty()); 
	}
	
	@Test
	public void testRandomEqualsCountElementsToFirstArg() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(10,20).size()==10); 
	}
}
