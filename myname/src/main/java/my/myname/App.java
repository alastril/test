package my.myname;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Validator;

import org.joda.time.DateTime;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import my.myname.anotations.AnotationFormater;
import my.myname.aop.AspectTest;
import my.myname.aop.PointCut;
import my.myname.aop.SimpleBean;
import my.myname.crud_spr_data.interfaces.AnimalsService;
import my.myname.crud_spr_data.interfaces.FoodService;
import my.myname.crud_spr_data.interfaces.ZooDao;
import my.myname.entity.Animals;
import my.myname.entity.Food;
import my.myname.entity.Zoo;
import my.myname.shedulers.ClassForShedulerTest;
import my.myname.shedulers.ShedulerAndAsync;
import my.myname.shedulers.TaskToExecute;
import my.myname.validation.converters.AnotherTypeForConvert;
import my.myname.validation.formaters.FormaterForAnotation;
import my.myname.validation.formaters.TestAnotationFormatter;
import my.myname.validation.validators.ValidatorImpl;
import my.myname.validation.validators.class_test.ClassforValidationTests;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Throwable {
		ApplicationContext ap = new GenericXmlApplicationContext("classpath:app-config.xml");

		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		FoodService fs = (FoodService) ap.getBean("FoodService");

		// ZooSaveCall(ap);
		 FoodSaveCall(ap);
		// formatersCall(ap);
		// JTACall(ap);
		// converterCall(ap);
		// AopCall(ap);
		//validatorsCall(ap);
		//asyncCall(ap);
		//executeTask(ap);
		 httpInvoker(ap);//need deploy application to servlet container(tomcat)
//		while(true) {
//			
//		}
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
		Zoo zoo = new Zoo();
		zoo.setName("menskiy");
		zoo.setDateCreation(new Date());
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
		zd.deleteZoo(zoo);
	}

	/**
	 * not working EntityManager with many-to-one relation when we try save
	 * children with parent(error: first save parent than children) <br/>
	 * but with session is okay
	 * 
	 * @param ap
	 * @param fs
	 * @param zd
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

		ZooDao zd = (ZooDao) ap.getBean("ZooDao");
		Animals an = new Animals();
		an.setName("sobaken");
		Animals an2 = new Animals();
		an2.setName("kotyara");
		zd.saveJTA(an, an2);
		zd.saveJTA((Animals) an2.clone(), (Animals) an.clone());
		List<Animals> al = zd.getListAnimals();
		for (Animals a : al) {
			System.out.println(a.toString());
			for (Food fod : a.getFoodList()) {
				System.out.println(fod.toString());
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
		ConversionService conversionService = ap.getBean(ConversionService.class);
		System.out.println(conversionService.convert(smpl, String.class));// print
		smpl = conversionService.convert("14-05-2036", SimpleBeanImpl.class); // parse

		conversionService.convert("14-05-2036", DateTime.class);

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
		Validator validatorJsr =  ap.getBean(LocalValidatorFactoryBean.class);
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
}
