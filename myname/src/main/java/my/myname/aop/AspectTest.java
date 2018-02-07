package my.myname.aop;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import my.myname.mvc.MarshUnmarsh;
import my.myname.validation.validators.class_test.ValidationTestMarker;

@Aspect
@Component
public class AspectTest {
	private static final Logger log = Logger.getLogger(AspectTest.class);
	@Around("execution(* my.myname.crud_spr_data.*.save(..))")
	public Object callForHello(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println(
				"<<<<<----------begin " + joinPoint.getSignature().getName().toUpperCase() + "----------->>>>>>>>>>");
		long before = System.currentTimeMillis();
		Object res = joinPoint.proceed();
		long after = System.currentTimeMillis();
		System.out.println("<<<<<<<<-- " + joinPoint.getSignature().getName() + "=>"
				+ joinPoint.getSignature().getDeclaringType() + " end, =>" + (after - before) + " ms-->>>>>");
		return res;
	}

	@Before("execution(* my.myname.crud_spr_data.*.save(my.myname.crud_spr_data.entity.Food || my.myname.crud_spr_data.entity.Animals)) && args(object)")
	public void callForZoo(JoinPoint joinPoint, Object object) throws Throwable {
		System.out.println(joinPoint.getSignature().getDeclaringType());
		Arrays.asList(joinPoint.getArgs()).stream().forEach(action -> {
			System.out.println("args:[" + action.toString() + "]");
		});
	}

	@Around("execution(* my.myname.validation..*.convert(..)) || execution(* my.myname.validation..*.parse(..)) || execution(* my.myname.validation..*.getParser(..))")
	public Object loggingFormatersAndConverters(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println();
		System.out.println("<<<<<<begin " + joinPoint.getSignature().getName().toUpperCase());
		System.out.println("[Args]: ");
		for (Object arg : joinPoint.getArgs()) {
			System.out.println(arg.toString());
		}
		long before = System.currentTimeMillis();
		Object res = joinPoint.proceed();
		long after = System.currentTimeMillis();
		System.out.println("[Result]: " + res.toString());
		System.out.println("<<<<<<<<-- " + joinPoint.getSignature().getName() + "=>"
				+ joinPoint.getSignature().getDeclaringType() + " end, =>" + (after - before) + " ms-->>>>>");
		System.out.println();
		return res;
	}

	@Around("execution(* my.myname.validation.validators.*.validate(..)) || execution(* javax.validation.Validator.validate(..))")
	public Object loggingValidators(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println();
		System.out.println("Class validation is -> " + joinPoint.getSignature().getDeclaringType());
		System.out.println("[Args]: ");

		long before = System.currentTimeMillis();
		Object res = joinPoint.proceed();
		long after = System.currentTimeMillis();
		for (Object arg : joinPoint.getArgs()) {
			System.out.println("" + arg);
			if (arg.getClass().equals(BeanPropertyBindingResult.class)) {
				BeanPropertyBindingResult bindRes = (BeanPropertyBindingResult) arg;
				bindRes.getAllErrors().stream().forEach(action -> {
					System.out.println("Spring Validator check:");
					System.out.println(action.getCode());
				});
			}
		}
		System.out.println("" + res);
		if (res != null && res.getClass().equals(HashSet.class)) {
			HashSet<ConstraintViolationImpl<? extends ValidationTestMarker>> conViolRes = (HashSet<ConstraintViolationImpl<? extends ValidationTestMarker>>) res;
			conViolRes.stream().forEach(action -> {
				System.out.println("Field name: " + action.getPropertyPath());
				System.out.println("Field value: " + action.getInvalidValue());
				System.out.println("Error message:" + action.getMessage());
			});
		}
		System.out.println("<<<<<<<<-- " + joinPoint.getSignature().getName() + "=>"
				+ joinPoint.getSignature().getDeclaringType() + " end, =>" + (after - before) + " ms-->>>>>");
		System.out.println();
		return res;
	}

	/**
	 * its need when after unmarshaling save entity-parent with entity-children(OneToMany). In bidirectional relation - 
	 * children also have parent(ManyToOne) but jaxB don't set object links of parent entities for children.
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("execution(* my.myname.mvc.*.set*(my.myname.mvc.MarshUnmarsh))")
	public void callForBidirectionalLinksAfterUnmarshaling(JoinPoint joinPoint) throws Throwable {
		log.info(joinPoint.getSignature().getDeclaringType());
		log.info("Begin do links for food to their parents");
		Arrays.asList(joinPoint.getArgs()).stream().forEach(obj -> {
			if (obj.getClass().getSimpleName().equals("MarshUnmarsh")) {
				MarshUnmarsh marshUnmarsh = (MarshUnmarsh) obj;
				marshUnmarsh.getZooList().stream().forEach(zoo -> {
					zoo.getAnimalsList().stream().forEach(animls -> {
						animls.getFoodList().stream().forEach(food -> {
							food.setAnimals(animls);
						});
					});
				});
			}
		});
	}

}
