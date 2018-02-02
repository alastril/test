package my.myname.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MyJmsListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		ActiveMQTextMessage ms = (ActiveMQTextMessage)message;
		try {
			System.out.println("LOL: "+ ms.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
