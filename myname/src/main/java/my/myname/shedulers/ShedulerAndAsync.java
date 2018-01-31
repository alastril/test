package my.myname.shedulers;

import java.util.concurrent.CompletableFuture;

import org.joda.time.DateTime;

public interface ShedulerAndAsync {
	public void methodForSheduler();
	public void methodForAsyncTest();
	public CompletableFuture<DateTime> methodForAsyncTestWithParamsAndRes(String date);
}
