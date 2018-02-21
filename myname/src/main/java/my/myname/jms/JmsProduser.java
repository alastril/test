package my.myname.jms;

import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JmsProduser {
	@Autowired
	@Qualifier("jmsTemplateAtomicos")
	JmsTemplate jms;
	
	@Autowired
	Queue queueDest;
	
	@Autowired
	Topic topicDest;

	@Transactional(transactionManager = "transactionМanagerAtomicos", rollbackFor = Exception.class)
	public void sendToTopic(String msg) {
		jms.convertAndSend(topicDest, msg);
	}
	@Transactional(transactionManager = "transactionМanagerAtomicos", rollbackFor = Exception.class)
	public void sendToQueue(String msg) {
		jms.convertAndSend(queueDest, msg);
	}

	public JmsTemplate getJms() {
		return jms;
	}

	public void setJms(JmsTemplate jms) {
		this.jms = jms;
	}
	public Queue getQueueDest() {
		return queueDest;
	}
	public void setQueueDest(Queue queueDest) {
		this.queueDest = queueDest;
	}
	public Topic getTopicDest() {
		return topicDest;
	}
	public void setTopicDest(Topic topicDest) {
		this.topicDest = topicDest;
	}


	
}
