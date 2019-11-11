package my.myname;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Validator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.convert.ConversionService;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.message.AdviceMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import my.myname.aop.PointCut;
import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;
import my.myname.crud_spr_data.interfaces.AnimalsService;
import my.myname.crud_spr_data.interfaces.FoodService;
import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.jms.JmsProduser;
import my.myname.mvc.MarshUnmarsh;
import my.myname.mvc.security.dao.UserDao;
import my.myname.mvc.security.dao.UserRoleDao;
import my.myname.mvc.security.entity.User;
import my.myname.mvc.security.entity.UserRole;
import my.myname.shedulers.ShedulerAndAsync;
import my.myname.shedulers.TaskToExecute;
import my.myname.validation.converters.AnotherTypeForConvert;
import my.myname.validation.validators.ValidatorImpl;
import my.myname.validation.validators.class_test.ClassforValidationTests;
import my.myname.web_socket.EchoClientHandler;
import my.myname.web_socket.EchoServerHandler;
import sun.net.www.content.text.plain;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger LOG = Logger.getLogger(App.class);
	public static void main(String[] args) throws Throwable {
		 GenericXmlApplicationContext ap = new GenericXmlApplicationContext();
		 ConfigurableEnvironment env = ap.getEnvironment();
		    env.setActiveProfiles("work");
		     
		ap.load("classpath:config_xml/app-config.xml");
		ap.refresh();
		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		FoodService fs = (FoodService) ap.getBean("FoodService");

		UserDao udi = (UserDao) ap.getBean("UserRepository");
		UserRoleDao urdi = (UserRoleDao) ap.getBean("UserRoleRepository");

//		callWebSocket();
//		callSTOMPWebSocket();
//		callIntegrationSpring(ap);
//		callBatch(ap);
//		  callRestRequest(ap);
		 //ZooSaveCall(ap);
		// FoodSaveCall(ap); //need for httpInvoker
//		 formatersCall(ap);
		 JTACall(ap);
		// converterCall(ap);
		// AopCall(ap);
//		validatorsCall(ap);
		//asyncCall(ap);
		//executeTask(ap);
//		jmsCall(ap);
//		 httpInvoker(ap);//need deploy application to servlet container(tomcat)
//		 while(true) {
//		   
//			}
//		org.springframework.xd.jdbc.NamedColumnJdbcBatchItemWriter
//		org.springframework.batch.item.file.FlatFileItemReader
//		org.springframework.batch.item.file.mapping.DefaultLineMapper
	}

	public static void AopCall(ApplicationContext ap) {
		SimpleBeanImpl sb = (SimpleBeanImpl) ap.getBean("simpleBeanImpl");
		sb.sayHello("Grisha");
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(sb);
		pf.addAdvice(new PointCut());
		SimpleBeanImpl aopBean = (SimpleBeanImpl) pf.getProxy();
		System.out.println(aopBean.sayHello("Grisha!1!11"));
		System.out.println(aopBean.sayFuck("Grisha!1!11"));
	}

	public static void ZooSaveCall(ApplicationContext ap) throws Throwable {
		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		AnimalsService as = (AnimalsService) ap.getBean("AnimalsService");
		Zoo zoo = new Zoo();
		zoo.setName("menskiy");
		zoo.setDateCreation(new DateTime());
		Animals animalSobaka = new Animals();
		animalSobaka.setName("sobaka");
		Food food = new Food();
		food.setName("wiskas");
		Food food2 = new Food();
		food2.setName("milk");

		Animals animalKot = new Animals();
		animalKot.setName("kot");
		animalKot.addFood(food);
		animalKot.addFood(food2);
 
		zoo.getAnimalsList().add(animalSobaka);
		zoo.getAnimalsList().add(animalKot);
		//animalSobaka.getZooList().add(zoo);
		//animalKot.getZooList().add(zoo);

		Zoo zooClone = (Zoo) zoo.clone();

		zd.save(zoo);
		zd.save(zooClone);

		List<Zoo> zl = zd.getListZoo();
		System.out.println(zl.size());
		for (Zoo z : zl) {
			System.out.println(z.toString());
			for (Animals animals : z.getAnimalsList()) {
				System.out.println(animals.toString());
			}
		}
    
		//zd.deleteZoo(zoo);
		
	}

	/**
	 * not working EntityManager with many-to-one relation when we try save
	 * children with parent(error: first save parent than children) <br/>
	 * but with session is okay
	 * 
	 * @param ap
	 * @throws Throwable
	 */
	public static void FoodSaveCall(ApplicationContext ap) throws Throwable {

		// ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		FoodService fs = (FoodService) ap.getBean("FoodService");
		AnimalsService as = (AnimalsService) ap.getBean("AnimalsService");
		Food food = ap.getBean(Food.class);
		food.setName("wiskas");
		Food food2 = ap.getBean(Food.class);
		food2.setName("milk");

		Animals animalKot = ap.getBean(Animals.class);
		animalKot.setName("kot");
		Animals animalKot2 = ap.getBean(Animals.class);
		animalKot2.setName("kot2");
		animalKot.addFood(food2);
		animalKot2.addFood(food);
		// food2.setAnimals(animals);
		// zd.save(animalKot);
		// zd.save(animalKot2);
		Animals test = food.getAnimals();

		if (test != null) {
			as.save(food.getAnimals());
		} else {
			fs.save(food);
		}

		test = food2.getAnimals();
		if (test != null) {
			as.save(food2.getAnimals());
		} else {
			fs.save(food2);
		}

		List<Food> foodList = fs.findAll();
		for (Food fd : foodList) {
			System.out.println(fd.toString());
		}

	}

	public static void JTACall(ApplicationContext ap) throws Throwable {
//		org.hibernate.engine.transaction.jta.platfo
		
		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		Animals an = new Animals();
		an.setName("sobaken");
		Animals an2 = new Animals();
		an2.setName("kotyara");
		Animals postgres = new Animals();
		postgres.setName("postgres pes smerdyaschiy");
		//zd.save(an);
		zd.saveJTA(an, an2, postgres);
		zd.saveJTA(an2, an, postgres);
		List<Animals> al = zd.getListAnimals();
		for (Animals a : al) {
			System.out.println("a => "+a.toString());
			for (Food fod : a.getFoodList()) {
				System.out.println("f => "+fod.toString());
			}
		}
	}

	public static void converterCall(ApplicationContext ap) {
		SimpleBeanImpl smpl = (SimpleBeanImpl) ap.getBean("dateConvert");
		System.out.println(smpl);
		ConversionService conversionService = ap.getBean(ConversionService.class);
		AnotherTypeForConvert anotherContact = ap.getBean(AnotherTypeForConvert.class);
		anotherContact.setId(25);
		anotherContact.setName("Vasya Pypkin");
		anotherContact.setDateTime(new DateTime());
		smpl = conversionService.convert(anotherContact, SimpleBeanImpl.class);
		System.out.println(smpl);
	}

	public static void formatersCall(ApplicationContext ap) {
		SimpleBeanImpl smpl = (SimpleBeanImpl) ap.getBean("simpleBeanImpl");

		System.out.println(smpl);

		// Formater
		ConversionService conversionService = (ConversionService)ap.getBean("conversionService");
		System.out.println(conversionService.convert(smpl, String.class));// print
		smpl = conversionService.convert("14-05-2036", SimpleBeanImpl.class); // parse

		conversionService.convert("10-05-1036", DateTime.class);

		// TestAnotationFormatter
		DataBinder dataBinder = new DataBinder(smpl);
		dataBinder.setConversionService(conversionService);
		MutablePropertyValues mpv = new MutablePropertyValues();
		mpv.add("name", "FACK!!!!");
		dataBinder.bind(mpv);
		dataBinder.getBindingResult().getModel().entrySet().forEach(System.out::println);
		System.out.println(smpl);
	}

	public static void validatorsCall(ApplicationContext ap) {
		ValidatorImpl validatorImpl = ap.getBean(ValidatorImpl.class);
		ClassforValidationTests classforValidationTests = ap.getBean(ClassforValidationTests.class);
		
		// Spring realization
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(classforValidationTests, "nameValidation");
		ValidationUtils.invokeValidator(validatorImpl, classforValidationTests, result);

//		result.getAllErrors().stream().forEach(action -> {
//			System.out.println("Spring Validator check:");
//			System.out.println(action.getCode());
//		});
		
		// JSR realization and //constraint realization
		classforValidationTests.setName("Myname!!");
		Validator validatorJsr =  (LocalValidatorFactoryBean)ap.getBean("validatorJSR_303");
		validatorJsr.validate(classforValidationTests);

		
		classforValidationTests.setName("A");//size annotation
		classforValidationTests.setDt(new DateTime());
		validatorJsr.validate(classforValidationTests);
//		.stream().forEach(action -> {
//			System.out.println(action.getPropertyPath());
//			System.out.println(action.getInvalidValue());
//			System.out.println(action.getMessage());
//		});
	}
	
	public static void asyncCall(ApplicationContext ap) throws InterruptedException, ExecutionException {
		ShedulerAndAsync shClass = (ShedulerAndAsync)ap.getBean("classForShedulerTest");
		long before = System.currentTimeMillis();
		shClass.methodForAsyncTest();
		shClass.methodForAsyncTest();
		shClass.methodForAsyncTest();
		long after = System.currentTimeMillis();
		System.out.println(after-before);
//		for(int i=0;i<10;i++) {
		 before = System.currentTimeMillis();
		CompletableFuture<DateTime> frst = shClass.methodForAsyncTestWithParamsAndRes("01-01-1000");
		CompletableFuture<DateTime> scnd = shClass.methodForAsyncTestWithParamsAndRes("01-01-2400");
		CompletableFuture<DateTime> trd = shClass.methodForAsyncTestWithParamsAndRes("15-11-2200");
		CompletableFuture.allOf(frst,scnd,trd).join();
		 after = System.currentTimeMillis();
		System.out.println(after-before);
//		}
		System.out.println(frst.get());
		System.out.println(scnd.get());
		System.out.println(trd.get());
 
	}
	
	public static void executeTask(ApplicationContext ap) {
		//Thread task
		TaskToExecute taskToExecute = ap.getBean(TaskToExecute.class);
		taskToExecute.executeTask(10);
	}
	
	public static void httpInvoker(ApplicationContext ap) {
		System.out.println("httpInvoker!!!");

		FoodService fs = (FoodService) ap.getBean("remoteFoodService");
		for(Food food: fs.findAll()){
			System.out.println("food:" + food);
		};

	}
	
	public static void jmsCall(ApplicationContext ap) {
		JmsProduser jp = (JmsProduser)ap.getBean("jmsProduser");
		jp.sendToQueue("HElllo!!!!1111");
		jp.sendToTopic("HElllo!!!!1111");
	}
	
	public static void callRestRequest(ApplicationContext ap) {
		RestTemplate rt = ap.getBean(RestTemplate.class);
		System.out.println("callRestTemplate");
 
		//get all zoo
//		rt.getForObject("http://localhost:8080/myname/restful/zoo/xmlzoo", MarshUnmarsh.class).getZooList().stream().forEach(action->{System.out.println(action.toString());});;
		 
		//add new zoo
		MarshUnmarsh m = new MarshUnmarsh();
		Zoo z = new Zoo();
		z.setName("ZOOOOO");
		z.setDateCreation(new DateTime());
		m.getZooList().add(z);
		rt.postForObject("http://localhost:8080/myname/restful/zoo/xmlzoo", m, Void.class);
		m = new MarshUnmarsh();
		z = new Zoo();
		z.setName("ZOOOOO");
		z.setDateCreation(new DateTime());
		m.getZooList().add(z);
		rt.postForObject("http://localhost:8080/myname/restful/zoo/jsonzoo", m, Void.class);


	} 
	
	public static void callBatch(ApplicationContext ap) {
		Job job = ap.getBean(Job.class);
		JobLauncher jobLauncher = ap.getBean(JobLauncher.class);
		JobParameters jParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
		try {
			jobLauncher.run(job, jParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
	
	public static void callIntegrationSpring(ApplicationContext ap) {
		MessageChannel input = (MessageChannel) ap.getBean("chanelInput");
		PollableChannel output = (PollableChannel) ap.getBean("chanelOutput");
		input.send(new GenericMessage<String>("vasya pypkin!"));
	    Message<?> reply = output.receive();
	    System.out.println("received: " + reply);
	}
	
	public static void callWebSocket() {
		System.out.println();
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		
		//WebSocketHandlers test
		ListenableFuture<WebSocketSession> test =  webSocketClient.doHandshake(
				new EchoClientHandler(), "ws://localhost:8080/myname/testJava",
				new Object[] {});
		try {
			
			test.get().sendMessage(new TextMessage("hello testJava".getBytes()));
			Thread.currentThread().sleep(500);//wait end all operations
			test.get().close();
			//for spring-config need add mvc path("/restful")
			test =  webSocketClient.doHandshake(new EchoClientHandler(), "ws://localhost:8080/myname/restful/websocket", new Object[] {});
			test.get().sendMessage(new TextMessage("Hello server!".getBytes()));
			Thread.currentThread().sleep(500);//wait end all operations
			test.get().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	private static void callSTOMPWebSocket() { 
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(webSocketClient));
		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new StringMessageConverter());
		
		//add inner subscriber after connection
		StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				System.out.println("Connection to STOMP established.");
				session.subscribe("/topic/pointToSend", new StompFrameHandler() {// "/topic"- broker address, "/pointToSend"- directly destination of messages
				    @Override
				    public Type getPayloadType(StompHeaders headers) {
				        return String.class;
				    }

				    @Override
				    public void handleFrame(StompHeaders headers,Object payload) {
				        System.out.println("Subscriber has recive: " +payload.toString());
				    }
				});
			}

			@Override
			public void handleException(StompSession session, StompCommand command, StompHeaders headers,
					byte[] payload, Throwable exception) {
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		};
		 
		try {  
			StompSession ses = stompClient.connect("ws://localhost:8080/myname/restful/stompEndPoint", sessionHandler).get();
			Thread.currentThread().sleep(100);//wait when session will be built
			ses.send("/app/pointToSend", "Here is a Johny!!!!!!".getBytes());
			Thread.currentThread().sleep(15000);//for testing from postman(as example), waiting response. Enough 100ms
			ses.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
