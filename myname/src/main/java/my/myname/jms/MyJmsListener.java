package my.myname.jms;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class MyJmsListener implements MessageListener{
	


	public MyJmsListener(@Value("${jms.queue.name}")final String queueName, @Value("${jms.topic.name}")final String topicName) {
	}
	
	@Override
	/**
	 * this method(interface implementation) need for <jms:listener-container>
	 */
	public void onMessage(Message message) {
		ActiveMQTextMessage ms = (ActiveMQTextMessage)message;
		try {
			System.out.println("LOL: "+ ms.getText() +", destination: "+ ms.getDestination());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this alternative for realization of listeners DefaultJmsListenerContainerFactory, in config must be namespace jms:annotation-driven
	 */
	@JmsListener(destination = "TopicqueueJms", containerFactory="jmsContainerForTopic")
	public void listenMessageTopic(Message message) {
		try {
			ActiveMQTextMessage ms = (ActiveMQTextMessage)message;
			System.out.println("Topic Message: "+ ms.getText()  +", destination:"+ ms.getDestination());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	@JmsListener(destination = "queueJms", containerFactory="jmsContainerForQueue")
	public void listenMessageQueue(Message message) {
		try {
		ActiveMQTextMessage ms = (ActiveMQTextMessage)message;
			System.out.println("Queue Message: "+ ms.getText()  +", destination:"+ ms.getDestination());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}



}
