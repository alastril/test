package my.myname.web_socket;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EchoClientHandler extends TextWebSocketHandler{
	private static final Logger LOG = Logger.getLogger(EchoClientHandler.class);
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		LOG.debug("Client WEBSOCKET!!! resive: " + message.getPayload());
		session.sendMessage(new TextMessage("thanks, goodbye!".getBytes()));
		session.close();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.debug("Client WEBSOCKET connection established!!!");
	}
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		LOG.debug("handleTextMessage!");
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		LOG.error(exception.getMessage());
		exception.printStackTrace();
	}
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOG.debug("Client WebSoket, connection closed: "+status.getReason() +", " + status.getCode());
	}
}
