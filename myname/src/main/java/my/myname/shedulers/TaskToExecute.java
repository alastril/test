package my.myname.shedulers;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class TaskToExecute {
	private TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

	public void executeTask(int countExecute) {
		for (int i = 0; i < countExecute; i++) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("Task is Execute!!!=>" + Thread.currentThread().getName());
				}
			});
		}
		
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
