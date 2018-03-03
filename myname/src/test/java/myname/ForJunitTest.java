package myname;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import my.myname.junit.ForJunit;

public class ForJunitTest {

	
	@Test
	public void testRandomCount() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(10,200).size()>0); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRandomRangeIllegalArg() {
		ForJunit ju = new ForJunit();
		System.out.println(ju.getRandomByCount(100,10)); 
	}
	
	@Test
	public void testRandomCountNotNull() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(0,0).isEmpty()); 
	}
	
	@Test
	public void testRandomEqualsCountElementsToFirstArg() {
		ForJunit ju = new ForJunit();
		assertTrue(ju.getRandomByCount(10,20).size()==10); 
	}
	
	public void testOauth() {
		 ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
	        resourceDetails.setUsername("roy");
	        resourceDetails.setPassword("spring");
	        resourceDetails.setAccessTokenUri("http://localhost/oauth/token");
	        resourceDetails.setClientId("key");
	        resourceDetails.setClientSecret("123456");
	        resourceDetails.setGrantType("password");
	        resourceDetails.setScope(Arrays.asList("read", "write"));

	        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

	        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
	        restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));

	        final String greeting = restTemplate.getForObject("http://localhost/greeting", String.class);

	        System.out.println(greeting);
	}
}
