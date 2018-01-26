package my.myname.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PointCut implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		System.out.println("invoking method..."+ arg0.getMethod().getName());
		String obj = (String)arg0.proceed();
		obj +=" <<Aop proccessed>>"; 
		System.out.println("End working of "+ arg0.getMethod().getName());
		return obj;
	}

}
