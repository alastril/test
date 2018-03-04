package my.myname.shedulers;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;




/**
 * The Class ClassForShedulerTest.
 * @Async good for parallel calls to another API where response take long time(example: send some information to server w\o result waiting)
 *  but for parallel calculating power it take more resources, as result longer time execution
 */
@Component("classForShedulerTest")
public class ClassForShedulerTest implements ShedulerAndAsync{

	/** The id. */
	private long id;
	
	/** The name. */
	private String name;
	
	/** The dt. */
	private DateTime dt;

	/**
	 * Gets the dt.
	 *
	 * @return the dt
	 */
	public DateTime getDt() {
		return dt;
	}

	/**
	 * Sets the dt.
	 *
	 * @param dt the new dt
	 */
	public void setDt(DateTime dt) {
		this.dt = dt;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see my.myname.shedulers.ShedulerAndAsync#methodForSheduler()
	 */
	//@Scheduled(cron="0/20 * * * * *")
	@Override
	public void methodForSheduler() {
		System.out.println("methodForSheduler!!!");
	}
	
	/* (non-Javadoc)
	 * @see my.myname.shedulers.ShedulerAndAsync#methodForAsyncTest()
	 */
	@Async
	@Override
	public void methodForAsyncTest() {
		for(int i=0; i<3000000; i++) {
			new Random().nextInt(100);
		}
	}

	/* (non-Javadoc)
	 * @see my.myname.shedulers.ShedulerAndAsync#methodForAsyncTestWithParamsAndRes(java.lang.String)
	 */
	@Override
	@Async
	public CompletableFuture<DateTime> methodForAsyncTestWithParamsAndRes(String date) {
		long before = System.currentTimeMillis();
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy");
		//slower @Async 
//		for(int i=0; i<100000; i++) {
//			new Random().nextInt(10);
//		}
		//Advantage of @Async - long time calls
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long after = System.currentTimeMillis();
		System.out.println("Done!!!!!" + (after-before));
		return CompletableFuture.completedFuture(dtf.parseDateTime(date));
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "id:" + getId() + ", name:" + getName() + ", date:" + getDt();
	}


}
