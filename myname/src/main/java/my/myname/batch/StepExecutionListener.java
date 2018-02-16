package my.myname.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class StepExecutionListener extends StepExecutionListenerSupport{
	private static final Logger LOG = Logger.getLogger(UserItemProcessor.class);
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOG.info("ReadCount: " + stepExecution.getReadCount() + ", WriteCount: " + stepExecution.getWriteCount());
		return null;
	}
}
