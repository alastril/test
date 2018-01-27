package my.myname.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AspectTest {

	
	@Around("execution(* my.myname.crud_spr_data.*.save(..)))")
	public Object callForHello(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("<<<<<----------begin "+joinPoint.getSignature().getName().toUpperCase()+"----------->>>>>>>>>>");
		long before = System.currentTimeMillis();
		Object res = joinPoint.proceed();
		long after = System.currentTimeMillis();
		System.out.println("<<<<<<<<-- "+joinPoint.getSignature().getName()+"=>"+ 
		joinPoint.getSignature().getDeclaringType() +" end, =>" + 
				(after-before) + " ms-->>>>>");
		return res;
	}
	
	
	@Before("execution(* my.myname.crud_spr_data.*.save(my.myname.entity.Food || my.myname.entity.Animals)) && args(object)")
	public void callForZoo(JoinPoint joinPoint, Object object) throws Throwable{
		System.out.println(joinPoint.getSignature().getDeclaringType());
		Arrays.asList(joinPoint.getArgs()).stream().forEach(action -> {System.out.println("args:["+action.toString() +"]");});
		
	}
}
