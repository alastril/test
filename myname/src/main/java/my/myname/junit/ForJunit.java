package my.myname.junit;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;


public class ForJunit {

	
	public Set<Integer> getRandomByCount(Integer count, Integer range) throws IllegalArgumentException{
			if(range<count || range<0)throw new IllegalArgumentException("Error: numberValueLimit < count; Need count>numberValueLimit!");
			Set<Integer>  setUnique = new TreeSet<>();
			Random r = new Random();
			for(int i=0; i<count; i++) {
				Integer current = r.nextInt(range);
				while(setUnique.contains(current)) {
					current = r.nextInt(range);
				}
				setUnique.add(current);
			}
		return setUnique;
	}
	
}
