package my.myname.web_socket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/testJava")
public class WebSocket {

	@OnOpen
	public void onOpen(){
	    System.out.println("MY_Open Connection ...");
	
	}
	
	@OnClose
	public void onClose(){
	    System.out.println("Close Connection ...");
	}
	
	@OnMessage
	public String onMessage(String message){
	    System.out.println("MY_Message from the client: " + message);
	    String echoMsg = "MY_Echo from the server : " + message;
	    return echoMsg;
	}
	@OnError
	public void onError(Throwable e){
		System.out.println(e.getMessage());
	    e.printStackTrace();
	}

}
