package myname;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;
import my.myname.junit.ForJunit;
import my.myname.mvc.ControllerZoo;
import my.myname.mvc.MarshUnmarsh;

public class ForJunitTest {
	private static final Logger log = Logger.getLogger(ForJunitTest.class);
	
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
	
	@Test
	public void testOauth() {
		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setUsername("pasha");
		resourceDetails.setPassword("baseSecret");
		resourceDetails.setAccessTokenUri("http://localhost:8080/myname/restful/oauth/token");
		resourceDetails.setClientId("pasha");
		resourceDetails.setClientSecret("123456");
		resourceDetails.setGrantType("password");
		resourceDetails.setScope(Arrays.asList("read", "write"));

		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
		try {
			OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
			Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
			jaxb2Marshaller.setClassesToBeBound(MarshUnmarsh.class, Zoo.class, Animals.class, Food.class);
			MarshallingHttpMessageConverter httpMessageConverter = new MarshallingHttpMessageConverter();
			httpMessageConverter.setMarshaller(jaxb2Marshaller);
			httpMessageConverter.setUnmarshaller(jaxb2Marshaller);
			restTemplate.setMessageConverters(Arrays.asList(httpMessageConverter));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_XML);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, headers);

			final ResponseEntity<MarshUnmarsh> xmlzoo = restTemplate.exchange(
					"http://localhost:8080/myname/restful/zoo/xmlzoo", HttpMethod.GET, requestEntity,
					MarshUnmarsh.class);

			System.out.println(xmlzoo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
