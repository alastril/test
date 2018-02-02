package my.myname.jms;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProduser {
	@Autowired
	JmsTemplate jms;
	
	@Autowired
	Queue destination;

	public void send(String msg) {
		jms.convertAndSend(destination, msg);
	}

	public JmsTemplate getJms() {
		return jms;
	}

	public void setJms(JmsTemplate jms) {
		this.jms = jms;
	}

	public Queue getDestination() {
		return destination;
	}

	public void setDestination(Queue destination) {
		this.destination = destination;
	}
	
}
