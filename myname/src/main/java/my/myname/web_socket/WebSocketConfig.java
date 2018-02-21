package my.myname.web_socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer { 
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	System.out.println("registerWebSocketHandlers...");
        registry.addHandler(new EchoServerHandler(), "/websocket")
            .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	System.out.println("registerStompEndpoints...");
    	registry.addEndpoint("/stompEndPoint").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	System.out.println("configureMessageBroker...");
    	registry.enableSimpleBroker("/topic");
    	registry.setApplicationDestinationPrefixes("/app");
    	
    }
}
