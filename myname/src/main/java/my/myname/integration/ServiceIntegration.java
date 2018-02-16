package my.myname.integration;

import org.springframework.stereotype.Component;

@Component
public class ServiceIntegration {

	
	public String getHelloUpper(String name) {
		return "Hello, "+name.toUpperCase();
	}
}
