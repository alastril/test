package my.myname.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import my.myname.Zoo;

@Aspect
@Component
public class AspectTest {

	
	@Before("execution(* javax.persistence.EntityManager.persist(..)) && args(my.myname.Zoo)")
	public void callForHello(JoinPoint joinPoint) throws Throwable{
		System.out.println("<<<<<<<<<<<<<<<<<<<<<--------------------------->>>>>>>>>>>>>>>>>>>>>");
		System.out.println(joinPoint.getSignature().getName());
		System.out.println("<<<<<<<<<<<<<<<<<<<<<--------------------------->>>>>>>>>>>>>>>>>>>>>");
		
	}
	
}
